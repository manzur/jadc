package code.skiplist;

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

	private ArrayList<Node<K, V>> headers;

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
			this.forward = new ArrayList<Node<K, V>>(level);

			clear();
		}

		private void clear() {
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
			V result = value;
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
		headers = new ArrayList();

		clear();
	}

	@Override
	public void clear() {
		for (int i = 0; i <= maxLevel; i++) {
			Node<K, V> node = new Node<K, V>(i);
			headers.add(node);
		}
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

		Node<K, V> node = getFirstHeader().forward.get(0);
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
		Node<K, V> node = getFirstHeader().forward.get(0);

		while (node != null) {
			result.add(node);
			node = node.forward.get(0);
		}

		return result;
	}

	@Override
	public boolean isEmpty() {
		return headers.size() > 0 && headers.get(0).forward != null;
	}

	@Override
	public Set<K> keySet() {
		final Set<K> result = new HashSet<K>();

		Node<K, V> node = getFirstHeader().forward.get(0);
		while (node != null) {
			result.add(node.getKey());
			node = node.forward.get(0);
		}

		return result;
	}

	@Override
	public V put(K key, V value) {
		Node<K, V>[] update = new Node[headers.size()];

		Node<K, V> node = getLastHeader();
		for (int i = maxLevel; i >= 0; i--) {
			while (node.forward.get(i) != null
					&& key.compareTo(node.forward.get(i).getKey()) != -1) {

				node = node.forward.get(i);
			}

			update[i] = node;
		}

		V result = null;

		if (key.equals(node.getKey())) {
			result = node.setValue(value);

		} else {
			int level = nodeLevel();
			Node<K, V> newNode = new Node<K, V>(level, key, value);

			for (int i = 0; i <= Math.min(level, maxLevel); i++) {
				Node<K, V> oldForward = update[i].forward.get(i);
				update[i].forward.set(i, newNode);
				newNode.forward.set(i, oldForward);
			}

			if (level > maxLevel) {
				Node<K, V> headerNode = new Node<K, V>(level);
				headerNode.forward.set(level, newNode);

				Node<K, V> previousNode = headers.get(maxLevel);
				for (int i = 0; i <= maxLevel; i++) {
					headerNode.forward.set(i, previousNode.forward.get(i));
				}

				headers.add(headerNode);

				maxLevel = level;

				assert maxLevel == headers.size() - 1;
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

		Node<K, V>[] update = new Node[headers.size()];

		Node<K, V> node = getLastHeader();
		for (int i = maxLevel; i >= 0; i--) {
			while (node.forward.get(i) != null
					&& key.compareTo(node.forward.get(i).getKey()) == 1) {

				node = node.forward.get(i);
			}

			update[i] = node;
		}

		final V result;
		if (node.forward.get(0) != null
				&& key.equals(node.forward.get(0).getKey())) {

			node = node.forward.get(0);
			result = node.getValue();

			int level = node.level;
			while (level >= 0) {
				update[level].forward.set(level, node.forward.get(level));
				level--;
			}

			if (node.level >= 0 && node.level == maxLevel
					&& headers.get(maxLevel).forward.get(maxLevel) == null) {

				headers.remove(maxLevel);
				maxLevel--;

			}
			if (maxLevel == -1) {
				maxLevel = 0;
				clear();
			}

			assert maxLevel == headers.size() - 1;

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
					Node<K, V> currentNode = getFirstHeader();

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
			builder.append(entry.getKey() + " " + entry.getValue() + ", ");
		}

		if (builder.length() > 2 && builder.charAt(builder.length() - 2) == ',') {
			builder.delete(builder.length() - 2, builder.length());
		}

		builder.append(" }");
		return builder.toString();
	}

	public String toDebugString() {

		StringBuilder builder = new StringBuilder();
		builder.append("{ ");

		for (int i = 0; i <= maxLevel; i++) {

			builder.append(i + " : " + "[");
			Node<K, V> entry = headers.get(i).forward.get(i);

			while (entry != null) {
				if (entry.level == i) {
					builder.append(entry.getKey() + " " + entry.getValue()
							+ ", ");
				}
				entry = entry.forward.get(i);
			}

			if (builder.length() > 2
					&& builder.charAt(builder.length() - 2) == ',') {
				builder.delete(builder.length() - 2, builder.length());
			}

			builder.append("]");
			builder.append("\n");
		}

		builder.append(" }");
		return builder.toString();

	}

	private Node<K, V> getLastHeader() {
		assert headers.size() > 0;

		Node<K, V> result = headers.get(headers.size() - 1);
		return result;
	}

	private Node<K, V> getFirstHeader() {
		assert headers.size() > 0;

		Node<K, V> result = headers.get(0);
		return result;
	}

	Optional<V> find(Object k) {
		assert k instanceof Comparable;

		Comparable<K> key = (Comparable<K>) k;
		Node<K, V> currentNode = getLastHeader();

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

	int nodeLevel() {
		int result = 0;

		Random random = new Random();
		while (random.nextBoolean() && result <= maxLevel) {
			result++;
		}

		return result;
	}
}
