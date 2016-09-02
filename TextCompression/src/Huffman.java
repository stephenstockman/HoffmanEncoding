import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

class Node
{

	public int character;
	public int frequency;
	public int pos;
	public int parent;
	public String prefix;
	public char letter;

	public Node(int c, int freq)// TODO: implement searching algoritm perhaps
								// use freq/binary to search
	{
		frequency = freq;
		character = c;
		letter = (char) c;
		pos = 99;
		parent = 999;
		prefix = "";
	}
}

class NodeFreqComparator implements Comparator<Node>
{
	public int compare(Node b1, Node b2)
	{
		return b1.frequency - b2.frequency;
	}
}

public class Huffman
{

	public static ArrayList<Node> NodeList = new ArrayList<Node>();
	public static ArrayList<Node> NodeDList = new ArrayList<Node>();
	public static ArrayList<Node> TreeList = new ArrayList<Node>();
	public static int rootNodeFreq = 0;
	public static int keyLength = 0;

	public static void createNodeList()
	{
		for (int index = 0; index < 256; index++)
		{
			NodeList.add(new Node(index, 0));
		}
	}

	public static void fillNodeList(String s)
	{
		for (char c : s.toCharArray())
		{
			for (Node n : NodeList)
			{
				if (c == n.character)
				{
					n.frequency++;
				}
			}
		}
	}

	public static void removeEmptyNode()
	{
		ArrayList<Node> tempNodeList = new ArrayList<Node>();
		for (Node n : NodeList)
		{
			if (n.frequency != 0)
			{
				tempNodeList.add(n);
			}
		}
		NodeList.clear();
		for (Node n : tempNodeList)
		{
			NodeList.add(n);
		}
	}

	public static void createTree() throws ConcurrentModificationException
	{
		int uid = 1;
		for (int i = 0; i < (NodeList.size() - 1) * 2; i++)
		{

			TreeList.get(0 + i).pos = 0;// left
			TreeList.get(1 + i).pos = 1;// right
			TreeList.get(0 + i).parent = uid;
			TreeList.get(1 + i).parent = uid;

			TreeList.add(new Node(uid, (TreeList.get(0 + i).frequency + TreeList.get(1 + i).frequency)));
			Collections.sort(TreeList, new NodeFreqComparator());
			uid++;
			i++;

		}
	}

	public static String createCode(Node b)
	{
		Node parent = null;
		String prefix = "";
		do
		{

			for (Node t : TreeList)
			{
				if (b.parent == t.character)
				{
					parent = t;
				}
			}
			if (parent.frequency != rootNodeFreq)
			{
				prefix = String.valueOf(parent.pos) + prefix;
				b = parent;
			}
		} while (parent.frequency != rootNodeFreq);
		return prefix;
	}

	public static String createEncString(String encString)

	{

		int indexA = 0;
		int indexB = 0;
		Node A = null;
		Node B = null;
		for (Node n : NodeList)
		{
			if (n.character == 48)
			{
				encString = encString.replaceAll("[" + '0' + "]", "/A");
				indexA = NodeList.indexOf(n);
				A = new Node(n.character, n.frequency);
				A.prefix = NodeList.get(indexA).prefix;
				System.out.println("/A: " + encString);
			}
			if (n.character == 49)
			{
				encString = encString.replaceAll("[" + '1' + "]", "/B");
				indexB = NodeList.indexOf(n);
				B = new Node(n.character, n.frequency);
				B.prefix = NodeList.get(indexB).prefix;
				System.out.println("/B: " + encString);
			}
		}

		if (encString.contains("/A"))
		{
			String prefixA = NodeList.get(indexA).prefix;
			encString = encString.replaceAll("/A", prefixA);
			NodeList.remove(indexA);
			System.out.println("/Apost: " + encString);
		}

		if (encString.contains("/B"))
		{
			String prefixB;
			if (indexA < indexB)
			{
				prefixB = NodeList.get(indexB - 1).prefix;
				NodeList.remove(indexB - 1);
			}
			else
			{
				prefixB = NodeList.get(indexB).prefix;
				NodeList.remove(indexB);
			}
			encString = encString.replaceAll("/B", prefixB);
			System.out.println("/Bpost: " + encString);
		}

		for (Node n : NodeList)//TODO: regex throws errors bc of special charaters
		{
			encString = encString.replaceAll("[" + Character.toString(n.letter) + "]", n.prefix);
		}
		System.out.println(encString);
		keyLength = encString.length();
		if (indexA != 0)
		{
			NodeList.add(A);
		}
		if (indexB != 0)
		{
			NodeList.add(B);
		}
		return encString;

	}

	public static String createKeyFile()// TODO: Use bits instead of prefix to
										// save space, could do the same for
										// character as well
	{
		String key = "";
		key += keyLength + " ";
		for (Node n : NodeList)
		{
			key += n.character + " " + n.prefix + " ";
		}
		System.out.println("key:" + key);
		return key;

	}

	public static String encodeHuffman(String s)
	{
		createNodeList();
		fillNodeList(s);
		removeEmptyNode();

		Collections.sort(NodeList, new NodeFreqComparator());

		for (Node n : NodeList)
			rootNodeFreq += n.frequency;

		TreeList = new ArrayList<Node>(NodeList);

		createTree();

		for (Node b : NodeList)
		{
			b.prefix = createCode(b) + String.valueOf(b.pos);
		}

		System.out.println("character" + "\t" + "frequency" + "\t" + "pos" + "\t\t" + "parent" + "\t\t" + "prefix" + "\t\t" + "letter");
		for (Node b : TreeList)
		{
			System.out.println(b.character + "\t\t" + b.frequency + "\t\t" + b.pos + "\t\t" + b.parent + "\t\t" + b.prefix + "\t\t" + b.letter);
		}
		return createEncString(s);
	}

	public static void decodeKey(File file)
	{
		String fileDir = file.getParent();
		Scanner fileKeyReader = null;
		int chara = 0;
		String pref = "";

		try
		{
			fileKeyReader = new Scanner(new File(fileDir, file.getName().substring(0, file.getName().length() - 4) + "key.shs"));
		} catch (Exception e)
		{
			System.out.println("File not found +" + file.getName().substring(0, file.getName().length() - 4) + "key.shs");
		}
		NodeDList.clear();
		int i = 0;
		keyLength = fileKeyReader.nextInt();
		while (fileKeyReader.hasNextInt())
		{
			chara = fileKeyReader.nextInt();
			pref = fileKeyReader.next();
			NodeDList.add(new Node(chara, 0));
			NodeDList.get(i).prefix = "" + pref;
			i++;
		}
		fileKeyReader.close();

	}

	public static String decodeHuffman(File file)// TODO: make hella
													// fast-already made 15%
													// faster-needs to be
													// 4000%... to match
													// compression
	{
		decodeKey(file);

		String binS = "";
		String output = "";
		String testCode = "";

		File binFile = new File("binFile");
		try
		{
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(binFile);
			FileChannel src = fis.getChannel();
			FileChannel dest = fos.getChannel();
			dest.transferFrom(src, 0, src.size());
			src.close();
			dest.close();
			fis.close();
			fos.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		BinaryIn binI = new BinaryIn("binFile");
		for (int t = 1; t <= keyLength; t++)
		{
			binS += (boolToInt(binI.readBoolean()));

		}

		long start = System.nanoTime();
		ArrayList<String> prefixList = new ArrayList<String>();
		for (Node b : NodeDList)
		{
			prefixList.add(b.prefix);
		}
		for (int j = 0; j < binS.length(); j++)// 14690487-4.79kb
		{
			testCode += binS.charAt(j);
			if (prefixList.contains(testCode))
			{
				output += NodeDList.get(prefixList.indexOf(testCode)).letter;
				testCode = "";
			}

		}
		prefixList.clear();
		long end = System.nanoTime();
		System.out.println(end - start);
		return output;

	}

	public static int boolToInt(boolean input)
	{
		if (input == true)
		{
			return 1;
		}

		return 0;
	}
}
