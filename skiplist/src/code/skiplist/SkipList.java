package code.skiplist;

import java.text.MessageFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Optional;

public class SkipList<K extends Comparable<K>, V> implements Map<K, V> {

	private int size;
	private int maxLevel;

	private Node<K, V> header;

	static class Node<K, V> implements Map.Entry<K, V> {
		int level;
		K key;
		V value;
		ArrayList<Node<K, V>> forward;

		Node(int level) {
			this(level, null, null);
		}

		Node(int level, K key, V value) {
			this.level = level;
			this.key = key;
			this.value = value;
			clear();
		}

		private void clear() {
			forward = new ArrayList<Node<K, V>>(level);

			for (int i = 0; i <= level; i++) {
				forward.add(null);
			}
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V result = this.value;
			this.value = value;
			return result;
		}

		@Override
		public String toString() {
			return "level = " + level + " key = " + key + " value = " + value;
		}
	}

	public SkipList() {
		maxLevel = 0;
		clear();
	}

	@Override
	public void clear() {
		header = new Node<K, V>(maxLevel);
	}

	@Override
	public boolean containsKey(Object k) {
		Optional<V> result = find(k);
		return result.isPresent();
	}

	@Override
	public V get(Object k) {
		Optional<V> result = find(k);
		return result.orNull();
	}

	@Override
	public boolean containsValue(Object value) {
		boolean result = false;

		Node<K, V> node = header.forward.get(0);
		while (node != null && !result) {
			V nodeValue = node.getValue();

			if (nodeValue == null) {
				result = nodeValue == value;
			} else {
				result = nodeValue.equals(value);
			}

			node = node.forward.get(0);
		}

		return result;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {

		Set<Map.Entry<K, V>> result = new HashSet<Map.Entry<K, V>>();
		Node<K, V> node = header.forward.get(0);

		while (node != null) {
			result.add(node);
			node = node.forward.get(0);
		}

		return result;
	}

	@Override
	public boolean isEmpty() {
		return header.forward.get(0) != null;
	}

	@Override
	public Set<K> keySet() {
		final Set<K> result = new HashSet<K>();

		Node<K, V> node = header.forward.get(0);
		while (node != null) {
			result.add(node.getKey());
			node = node.forward.get(0);
		}

		return result;
	}

	@Override
	public V put(K key, V value) {
		Node<K, V>[] update = new Node[header.forward.size()];

		Node<K, V> node = header;
		for (int i = maxLevel; i >= 0; i--) {
			while (node.forward.get(i) != null
					&& key.compareTo(node.forward.get(i).getKey()) == 1) {

				node = node.forward.get(i);
			}

			update[i] = node;
		}

		V result = null;

		if (node.forward.get(0) != null
				&& key.equals(node.forward.get(0).getKey())) {
			result = node.forward.get(0).setValue(value);

		} else {
			int level = nodeLevel();
			Node<K, V> newNode = new Node<K, V>(level, key, value);

			for (int i = 0; i <= Math.min(level, maxLevel); i++) {
				Node<K, V> oldForward = update[i].forward.get(i);
				update[i].forward.set(i, newNode);
				newNode.forward.set(i, oldForward);
			}

			if (level > maxLevel) {
				header.forward.add(newNode);

				maxLevel = level;

				assert maxLevel == header.forward.size() - 1;
			}

			size++;
		}

		return result;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public V remove(Object k) {
		assert k instanceof Comparable;
		K key = (K) k;

		Node<K, V>[] update = new Node[header.forward.size()];

		Node<K, V> node = header;
		for (int i = maxLevel; i >= 0; i--) {
			while (node.forward.get(i) != null
					&& key.compareTo(node.forward.get(i).getKey()) == 1) {

				node = node.forward.get(i);
			}

			update[i] = node;
		}

		final V result;
		node = node.forward.get(0);
		if (node != null && key.equals(node.getKey())) {

			result = node.getValue();

			int level = node.level;
			while (level >= 0) {
				update[level].forward.set(level, node.forward.get(level));
				level--;
			}

			if (node.level == maxLevel && header.forward.get(maxLevel) == null) {
				header.forward.remove(maxLevel);
				maxLevel--;
			}

			if (maxLevel == -1) {
				maxLevel = 0;
				clear();
			}

			assert maxLevel == header.forward.size() - 1;

			size--;
		} else {
			result = null;
		}

		return result;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		return new AbstractCollection<V>() {

			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {
					Node<K, V> currentNode = header;

					@Override
					public boolean hasNext() {
						return currentNode.forward.get(0) != null;
					}

					@Override
					public V next() {
						V result = currentNode.forward.get(0).getValue();
						currentNode = currentNode.forward.get(0);
						return result;
					}

					/*
					 * isn't supported as it won't be constant operation that
					 * user expects(optionally deprecated)
					 */
					@Deprecated
					@Override
					public void remove() {
						throw new UnsupportedOperationException(
								"Should use remove of the map rather");
					}

				};
			}

			@Override
			public int size() {
				return size;
			}

		};
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ ");

		Set<Entry<K, V>> entries = entrySet();
		for (Entry<K, V> entry : entries) {
			String s = MessageFormat.format("{0} {1}, ", entry.getKey(),
					entry.getValue());
			builder.append(s);
		}

		int lastIndex;
		if ((lastIndex = builder.lastIndexOf(",")) != -1) {
			builder.delete(lastIndex, lastIndex + 1);
		}

		builder.append(" }");
		return builder.toString();
	}

	public String toDebugString() {

		StringBuilder builder = new StringBuilder();
		builder.append("{ ");

		for (int i = 0; i <= maxLevel; i++) {

			builder.append(i + " : " + "[");
			Node<K, V> entry = header.forward.get(i);

			while (entry != null) {
				if (entry.level == i) {
					String s = MessageFormat.format("{0} {1}, ",
							entry.getKey(), entry.getValue());
					builder.append(s);
				}
				entry = entry.forward.get(i);
			}

			int lastIndex;
			if ((lastIndex = builder.lastIndexOf(",")) != -1) {
				builder.delete(lastIndex, lastIndex + 1);
			}

			builder.append("]");
			builder.append("\n");
		}

		builder.append(" }");
		return builder.toString();

	}

	/**
	 * finger search operation
	 * 
	 * @return
	 */
	public Searcher seacher() {
		return new Searcher<K, V>() {

			Node<K, V>[] fingers = new Node[header.forward.size()];
			{
				for (int i = 0; i < fingers.length; i++) {
					fingers[i] = header;
				}
			}

			@Override
			public V search(K key) {

				// no elements or we hit the bound on the previous searches
				if (size == 0 || fingers[0].forward.get(0) == null) {
					return null;
				}

				int level = -1;
				Node<K, V> cursor = header;

				if (key.compareTo(fingers[0].forward.get(0).getKey()) < 0) {

					for (int i = 0; i < fingers.length; i++) {

						if (fingers[i].forward.get(i) == null
								|| key.compareTo(fingers[i].forward.get(i)
										.getKey()) >= 0) {
							break;
						}

						level = i;
						cursor = fingers[i];
					}

				} else {

					for (int i = 0; i < fingers.length; i++) {

						if (fingers[i].forward.get(i) != null
								&& key.compareTo(fingers[i].forward.get(i)
										.getKey()) < 0) {

							level = i;
							cursor = fingers[i];
							break;
						}
					}

					if (level == -1) {
						level = fingers.length - 1;
						cursor = header;
					}
				}

				for (int i = level; i >= 0; i--) {
					while (cursor.forward.get(i) != null
							&& key.compareTo(cursor.forward.get(i).getKey()) > 0) {
						cursor = cursor.forward.get(i);
					}
					fingers[i] = cursor;
				}

				if (cursor.forward.get(0) != null
						&& key.compareTo(cursor.forward.get(0).getKey()) == 0) {
					return cursor.forward.get(0).getValue();
				}

				return null;
			}

		};
	}

	private Optional<V> find(Object k) {
		assert k instanceof Comparable;

		Comparable<K> key = (Comparable<K>) k;
		Node<K, V> currentNode = header;

		for (int i = maxLevel; i >= 0; i--) {
			while (currentNode.forward.get(i) != null
					&& key.compareTo(currentNode.forward.get(i).getKey()) != -1) {
				currentNode = currentNode.forward.get(i);
			}
		}

		final Optional<V> result;

		if (key.equals(currentNode.getKey())) {
			result = Optional.fromNullable(currentNode.getValue());

		} else {
			result = Optional.absent();
		}

		return result;
	}

	private int nodeLevel() {
		int result = 0;

		Random random = new Random();
		while (random.nextBoolean() && result <= maxLevel) {
			result++;
		}

		return result;
	}
}
