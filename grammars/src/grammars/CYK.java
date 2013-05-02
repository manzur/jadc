package grammars;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import clojure.lang.APersistentMap;
import clojure.lang.Keyword;
import clojure.lang.PersistentList;
import clojure.lang.PersistentVector;

public class CYK {
	private APersistentMap map;
	private PersistentVector words;
	private Keyword[][] matrix;
	private Integer[][] history;

	public CYK(APersistentMap map, PersistentVector words) {
		this.map = map;
		this.words = words;

		int n = words.size();
		matrix = new Keyword[n][n];
		history = new Integer[n][n];
	}

	/* CYK algorithm for recognizing CNF grammars */
	public void parse() {
		/* map from token to terminal */
		Map<String, Keyword> token2Terminals = new HashMap<>();

		try {
			token2Terminals = preprocess();

			initMatrix(token2Terminals);

			// printMatrix();

			for (int packLength = 2; packLength <= matrix.length; packLength++) {
				for (int start = 0; start <= matrix.length - packLength; start++) {
					int lastIndex = start + packLength - 1;

					for (int cursor = start; cursor < lastIndex; cursor++) {

						Keyword production1 = matrix[start][cursor];
						Keyword production2 = matrix[cursor + 1][lastIndex];

						Keyword production = searchForProduction(map,
								production1, production2);

						if (matrix[start][lastIndex] == null) {
							history[start][lastIndex] = cursor;
							matrix[start][lastIndex] = production;
						}
					}
				}
			}

			// printMatrix();

			printProduction(0, matrix.length - 1);

		} catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	private void printMatrix() {
		System.err.println("matrix");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.err.print(matrix[i][j] + "||");
			}
			System.err.println();
		}
	}

	private void initMatrix(Map<String, Keyword> token2Terminals) {
		for (int i = 0; i < matrix.length; i++) {
			String word = (String) words.get(i);
			Keyword keyword = token2Terminals.get(word);

			matrix[i][i] = keyword;
		}
	}

	private Map<String, Keyword> preprocess() {
		Map<String, Keyword> result = new HashMap<>();

		for (Object entry : map) {
			Map.Entry e = (Map.Entry) entry;

			Keyword k = (Keyword) e.getKey();
			PersistentList value = (PersistentList) e.getValue();

			for (Object o : value) {
				if (o instanceof String) {
					result.put((String) o, k);
				}
			}
		}

		return result;
	}

	private void printProduction(int row, int column) {

		Keyword current = matrix[row][column];
		Integer cursor = history[row][column];

		if (current != null && cursor != null) {

			Keyword pr1 = matrix[row][cursor];
			Keyword pr2 = matrix[cursor + 1][column];

			System.err.println(current + " -> " + pr1 + " + " + pr2);

			printProduction(row, cursor);
			printProduction(cursor + 1, column);

		} else if (current != null) {
			PersistentList list = (PersistentList) map.get(current);
			System.err.println(current + " -> " + words.get(row));
		}
	}

	private static Keyword searchForProduction(APersistentMap map, Keyword pr1,
			Keyword pr2) {

		Keyword result = null;

		L: for (Object object : map) {
			Map.Entry entry = (Entry) object;
			PersistentList productions = (PersistentList) entry.getValue();

			for (Object production : productions) {
				if (production instanceof PersistentVector) {
					PersistentVector vector = (PersistentVector) production;

					// checking for == 2 is redundant for CNF
					if (vector.size() == 2 && vector.get(0).equals(pr1)
							&& vector.get(1).equals(pr2)) {
						result = (Keyword) entry.getKey();
						break L;
					}
				}
			}

		}

		return result;
	}
}
