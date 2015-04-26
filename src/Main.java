import java.io.*;
import java.util.*;

public class Main
{
	static String TrainPathEnglish = "./data/corpus.en";
	static String TrainPathEspana = "./data/corpus.es";
	static String DevPathEnglish = "./data/dev.en";
	static String DevPathEspana = "./data/dev.es";
	static String OutPathIBM1 = "./data/dev.out1";
	static String OutPathIBM2 = "./data/dev.out2";
	static String OutPathIBM = "./data/dev.out3";

	static ArrayList<String> EnglishList = new ArrayList<String>();
	static ArrayList<String> EspanaList = new ArrayList<String>();

	static HashMap<WordPair, Double> WordPairCnt = new HashMap<WordPair, Double>();
	static HashMap<String, Double> WordCnt = new HashMap<String, Double>();
	static HashMap<IJLM, Double> IJLMCnt = new HashMap<IJLM, Double>();
	static HashMap<ILM, Double> ILMCnt = new HashMap<ILM, Double>();

	static boolean IBM2_First_Run = true;

	public static void Inc(HashMap<String, Double> wordCnt, String e,
			double delta) throws Exception
	{
		if (wordCnt.containsKey(e) == false)
		{
			wordCnt.put(e, delta);
		}
		else
		{
			wordCnt.put(e, wordCnt.get(e) + delta);
		}
	}

	public static void Inc(HashMap<WordPair, Double> wordPairCnt,
			WordPair pair, double delta) throws Exception
	{
		if (wordPairCnt.containsKey(pair) == false)
		{
			wordPairCnt.put(pair, delta);
		}
		else
		{
			wordPairCnt.put(pair, wordPairCnt.get(pair) + delta);
		}
	}

	public static void Inc(HashMap<IJLM, Double> wordPairCnt, IJLM pair,
			double delta) throws Exception
	{
		if (wordPairCnt.containsKey(pair) == false)
		{
			wordPairCnt.put(pair, delta);
		}
		else
		{
			wordPairCnt.put(pair, wordPairCnt.get(pair) + delta);
		}
	}

	public static void Inc(HashMap<ILM, Double> wordPairCnt, ILM pair,
			double delta) throws Exception
	{
		if (wordPairCnt.containsKey(pair) == false)
		{
			wordPairCnt.put(pair, delta);
		}
		else
		{
			wordPairCnt.put(pair, wordPairCnt.get(pair) + delta);
		}
	}

	// 初始化WordPairCnt，WordCnt，并读取训练语料
	public static void init(String englishPath, String espanaPath)
			throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(englishPath));
		String line = null;
		while ((line = br.readLine()) != null)
		{
			EnglishList.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(espanaPath));
		while ((line = br.readLine()) != null)
		{
			EspanaList.add(line);
		}
		br.close();

		for (int i = 0; i < EnglishList.size(); ++i)
		{
			String englishSentence = EnglishList.get(i);
			String[] E = englishSentence.split(" ");
			String espanaSentence = EspanaList.get(i);
			String[] F = espanaSentence.split(" ");
			for (int j = 0; j < E.length; ++j)
			{
				String e = E[j];
				for (int k = 0; k < F.length; ++k)
				{
					String f = F[k];
					WordPair pair = new WordPair(e, f);
					Inc(WordPairCnt, pair, 1d);
					Inc(WordCnt, e, 1d);
				}
			}
		}

	}

	static double T(String english, String espana) throws Exception
	{
		WordPair pair = new WordPair(english, espana);
		double wordPairVal = WordPairCnt.get(pair);
		double wordVal = WordCnt.get(english);
		return wordPairVal / wordVal;
	}

	public static void ibm1() throws Exception
	{
		// s为iteration
		for (int s = 0; s < 5; ++s)
		{
			System.out.println("ibm1 iteration : " + (s + 1));
			// E
			HashMap<WordPair, Double> wordPairCnt = new HashMap<WordPair, Double>();
			HashMap<String, Double> wordCnt = new HashMap<String, Double>();
			for (int k = 0; k < EnglishList.size(); ++k)
			{
				String[] e = EnglishList.get(k).split(" ");
				String[] f = EspanaList.get(k).split(" ");
				int l = e.length;
				int m = f.length;
				for (int i = 0; i < m; ++i) // f
				{
					double total = 0.0; // 作为计算delta的分母
					for (int j = 0; j < l; ++j) // e
					{
						total += T(e[j], f[i]);
					}

					for (int j = 0; j < l; ++j)
					{
						WordPair pair = new WordPair(e[j], f[i]);
						double delta = T(e[j], f[i]) / total;
						// Model 1
						Inc(wordPairCnt, pair, delta);
						Inc(wordCnt, e[j], delta);
					}
				}
			}

			// M
			WordPairCnt = wordPairCnt;
			WordCnt = wordCnt;
		}
	}

	public static int[] align_ibm1(String[] e, String[] f) throws Exception
	{
		int l = e.length;
		int m = f.length;
		int[] alignment = new int[m];
		for (int i = 0; i < m; ++i)
		{
			alignment[i] = -1;
		}

		for (int i = 0; i < m; ++i)
		{
			double max_pro = 0.0;
			int max_j = 0;
			for (int j = 0; j < l; ++j)
			{
				double pro = T(e[j], f[i]);
				if (pro > max_pro)
				{
					max_pro = pro;
					max_j = j;
				}
				System.out.println(pro);
			}
			if (max_pro > 0.0)
			{
				alignment[i] = max_j;
			}
		}
		return alignment;
	}

	public static void part1(String englishPath, String espanaPath,
			String resultPath) throws Exception
	{
		// align
		ArrayList<String> englishList = new ArrayList<String>();
		ArrayList<String> espanaList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(englishPath));
		String line = null;
		while ((line = br.readLine()) != null)
		{
			englishList.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(espanaPath));
		while ((line = br.readLine()) != null)
		{
			espanaList.add(line);
		}
		br.close();

		FileWriter fw = new FileWriter(resultPath);
		for (int i = 0; i < englishList.size(); ++i)
		{
			String english = englishList.get(i);
			String[] e = english.split(" ");
			String espana = espanaList.get(i);
			String[] f = espana.split(" ");
			int[] alignment = align_ibm1(e, f);

			for (int j = 0; j < alignment.length; ++j)
			{
				if (j != -1)
				{
					// sentNo  englishIndex  espanaIndex
					fw.write((i + 1) + " " + (alignment[j] + 1) + " " + (j + 1)
							+ "\n");
				}
			}
		}
		fw.close();
	}

	static double Q(int i, int j, int l, int m) throws Exception
	{
		if (IBM2_First_Run)
		{
			return 1.0 / (l + 1);
		}
		IJLM ijlm = new IJLM(i, j, l, m);
		ILM ilm = new ILM(i, l, m);
		return (double) IJLMCnt.get(ijlm) / ILMCnt.get(ilm);
	}

	public static void ibm2() throws Exception
	{
		// s为iteration
		for (int s = 0; s < 5; ++s)
		{
			System.out.println("ibm2 iteration : " + (s + 1));
			// E
			HashMap<WordPair, Double> wordPairCnt = new HashMap<WordPair, Double>();
			HashMap<String, Double> wordCnt = new HashMap<String, Double>();
			HashMap<IJLM, Double> ijlmCnt = new HashMap<IJLM, Double>();
			HashMap<ILM, Double> ilmCnt = new HashMap<ILM, Double>();

			for (int k = 0; k < EnglishList.size(); ++k)
			{
				String[] e = EnglishList.get(k).split(" ");
				String[] f = EspanaList.get(k).split(" ");
				int l = e.length;
				int m = f.length;
				for (int i = 0; i < m; ++i) // f
				{
					double total = 0.0; // 作为计算delta的分母
					for (int j = 0; j < l; ++j) // e
					{
						total += Q(i, j, l, m) * T(e[j], f[i]);
					}

					for (int j = 0; j < l; ++j)
					{
						WordPair pair = new WordPair(e[j], f[i]);
						double delta = Q(i, j, l, m) * T(e[j], f[i]) / total;
						// Model 1
						Inc(wordPairCnt, pair, delta);
						Inc(wordCnt, e[j], delta);
						//Model 2
						IJLM ijlm = new IJLM(i, j, l, m);
						Inc(ijlmCnt, ijlm, delta);
						ILM ilm = new ILM(i, l, m);
						Inc(ilmCnt, ilm, delta);
					}
				}
			}

			// M
			WordPairCnt = wordPairCnt;
			WordCnt = wordCnt;
			IJLMCnt = ijlmCnt;
			ILMCnt = ilmCnt;
			IBM2_First_Run = false;
		}
	}

	public static int[] align_ibm2(String[] e, String[] f) throws Exception
	{
		int l = e.length;
		int m = f.length;
		int[] alignment = new int[m];
		for (int i = 0; i < m; ++i)
		{
			alignment[i] = -1;
		}

		for (int i = 0; i < m; ++i)
		{
			double max_pro = 0.0;
			int max_j = 0;
			for (int j = 0; j < l; ++j)
			{
				double pro = T(e[j], f[i]) * Q(i, j, l, m);
				if (pro > max_pro)
				{
					max_pro = pro;
					max_j = j;
				}
			}
			if (max_pro > 0.0)
			{
				alignment[i] = max_j;
			}
		}
		return alignment;
	}

	public static void part2(String englishPath, String espanaPath,
			String resultPath) throws Exception
	{
		// align
		ArrayList<String> englishList = new ArrayList<String>();
		ArrayList<String> espanaList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(englishPath));
		String line = null;
		while ((line = br.readLine()) != null)
		{
			englishList.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(espanaPath));
		while ((line = br.readLine()) != null)
		{
			espanaList.add(line);
		}
		br.close();

		FileWriter fw = new FileWriter(resultPath);
		for (int i = 0; i < englishList.size(); ++i)
		{
			String english = englishList.get(i);
			String[] e = english.split(" ");
			String espana = espanaList.get(i);
			String[] f = espana.split(" ");
			int[] alignment = align_ibm2(e, f);

			for (int j = 0; j < alignment.length; ++j)
			{
				if (j != -1)
				{
					// sentNo  englishIndex  espanaIndex
					fw.write((i + 1) + " " + (alignment[j] + 1) + " " + (j + 1)
							+ "\n");
				}
			}
		}
		fw.close();
	}

	public static void heuristic(String srcPath, String dstPath,
			String heuristicPath) throws Exception
	{
		HashMap<String, HashSet<String>> englishMap = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> espanaMap = new HashMap<String, HashSet<String>>();

		BufferedReader br = new BufferedReader(new FileReader(srcPath));
		String line = null;
		while ((line = br.readLine()) != null)
		{
			String[] strs = line.split(" ");
			if (englishMap.containsKey(strs[0]) == false)
			{
				HashSet<String> set = new HashSet<String>();
				set.add(strs[1] + " " + strs[2]);
				englishMap.put(strs[0], set);
			}
			else
			{
				HashSet<String> set = englishMap.get(strs[0]);
				set.add(strs[1] + " " + strs[2]);
			}
		}
		br.close();

		br = new BufferedReader(new FileReader(dstPath));
		while ((line = br.readLine()) != null)
		{
			String[] strs = line.split(" ");
			if (espanaMap.containsKey(strs[0]) == false)
			{
				HashSet<String> set = new HashSet<String>();
				set.add(strs[2] + " " + strs[1]);
				espanaMap.put(strs[0], set);
			}
			else
			{
				HashSet<String> set = espanaMap.get(strs[0]);
				set.add(strs[2] + " " + strs[1]);
			}
		}
		br.close();

		FileWriter fw = new FileWriter(heuristicPath);
		for (int k = 1; k <= 200; ++k)
		{
			int l = 0, m = 0;
			HashSet<String> englishSet = englishMap.get(k + "");
			for (String str : englishSet)
			{
				String[] strs = str.split(" ");
				int englishIndex = Integer.parseInt(strs[0]);
				int espanaIndex = Integer.parseInt(strs[1]);
				if (englishIndex > l)
				{
					l = englishIndex;
				}
				if (espanaIndex > m)
				{
					m = espanaIndex;
				}
			}
			HashSet<String> espanaSet = espanaMap.get(k + "");
			for (String str : espanaSet)
			{
				String[] strs = str.split(" ");
				int englishIndex = Integer.parseInt(strs[0]);
				int espanaIndex = Integer.parseInt(strs[1]);
				if (englishIndex > l)
				{
					l = englishIndex;
				}
				if (espanaIndex > m)
				{
					m = espanaIndex;
				}
			}

			int[][] a = new int[l + 2][m + 2];
			for (int i = 0; i <= l + 1; ++i)
			{
				for (int j = 0; j <= m + 1; ++j)
				{
					a[i][j] = 0;
				}
			}

			for (String str : englishSet)
			{
				String[] strs = str.split(" ");
				int englishIndex = Integer.parseInt(strs[0]);
				int espanaIndex = Integer.parseInt(strs[1]);
				a[englishIndex][espanaIndex] += 1;
			}

			for (String str : espanaSet)
			{
				String[] strs = str.split(" ");
				int englishIndex = Integer.parseInt(strs[0]);
				int espanaIndex = Integer.parseInt(strs[1]);
				a[englishIndex][espanaIndex] += 1;
			}

			HashSet<String> resultSet = new HashSet<String>();
			// step 1 : 先加入双向对齐点
			for (int i = 1; i <= l; ++i)
			{
				for (int j = 1; j <= m; ++j)
				{
					int cnt = a[i][j];
					if (cnt == 2)
					{
						resultSet.add(i + " " + j);
					}
				}
			}

			// step 2 : 加入与双向对齐点相邻的单向对齐点
			for (int i = 1; i <= l; ++i)
			{
				for (int j = 1; j <= m; ++j)
				{
					int cnt = a[i][j];
					if (cnt == 1)
					{
						if (a[i - 1][j] == 2 || a[i + 1][j] == 2
								|| a[i][j - 1] == 2 || a[i][j + 1] == 2)
						{
							resultSet.add(i + " " + j);
						}
					}
				}
			}

			// step 3 : 如果存在单向对齐点，并且该点的两个词存在没有与任何词双向对齐
			for (int i = 1; i <= l; ++i)
			{
				for (int j = 1; j <= m; ++j)
				{
					int cnt = a[i][j];
					if (cnt == 1 && resultSet.contains(i + " " + j) == false)
					{
						boolean flag = true;
						for (int x = 1; x <= l; ++x)
						{
							if (a[x][j] == 2)
							{
								flag = false;
								break;
							}
						}
						for (int y = 1; y <= m; ++y)
						{
							if (a[i][y] == 2)
							{
								flag = false;
								break;
							}
						}
						if (flag == true)
						{
							resultSet.add(i + " " + j);
						}
					}
				}
			}

			for (String str : resultSet)
			{
				fw.write(k + " " + str + "\n");
			}
		}
		fw.close();
	}

	public static void main(String[] args) throws Exception
	{
		// 正向
				init(TrainPathEnglish, TrainPathEspana);
				ibm1();
				part1(DevPathEnglish, DevPathEspana, OutPathIBM1);
		//		ibm2();
		//		part2(DevPathEnglish, DevPathEspana, OutPathIBM2);

		String reversePath = "./data/dev.reverse";
		// 反向
		//		init(TrainPathEspana, TrainPathEnglish);
		//		ibm1();
		//		//		part1(DevPathEnglish, DevPathEspana, OutPathIBM1);
		//		ibm2();
		//		part2(DevPathEspana, DevPathEnglish, reversePath);

//		heuristic(OutPathIBM2, reversePath, "./data/dev.heuristic");

		System.exit(0);
	}

}
