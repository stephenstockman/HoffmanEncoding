import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class txtCompression
{
	public static void main(String[] args)
	{
		gui frame = new gui();
		frame.setVisible(true);
	}

	private static String readFile(String fileName)
	{
		String text = "";
		try
		{
			Scanner fileReader = new Scanner(new File(fileName));
			while (fileReader.hasNext())
			{
				text += fileReader.nextLine();
			}
			fileReader.close();
		} catch (IOException e)
		{
			System.out.println("Error reading file");
		}
		System.out.println(text);
		return text;
	}

	public static void lLCompressionTXT(File file)
	{
		String fileName = file.getAbsolutePath();
		String fileDir = file.getParent();
		String text = readFile(fileName);
		
		BinaryOut bin = null;
		try
		{
			bin = new BinaryOut(new File(fileDir,file.getName().substring(0, file.getName().length() - 4) + ".shs"));

		} catch (Exception e)
		{
			System.out.println("File can't be created");
		}

		String encode = Huffman.encodeHuffman(text);
		char[] output = encode.toCharArray();
		
		for (int i = 0; i < output.length; i++)
			{
				if (output[i] == '0')
				{
					bin.writeBit(false);
				}
				else if (output[i] == '1')
				{
					bin.writeBit(true);
				}
			}
			
		bin.flush();
		
		PrintWriter keywriter = null;
		try
		{
			keywriter = new PrintWriter(new File(fileDir, file.getName().substring(0, file.getName().length() - 4) + "key.shs"));

		} catch (Exception e)
		{
			System.out.println("File can't be created");
		}

		keywriter.print(Huffman.createKeyFile());
		keywriter.close();
	}

	public static void lLDCompressionTXT(File file)

	{
		String fileDir = file.getParent();
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(new File(fileDir, file.getName().substring(0, file.getName().length() - 4) + ".txt"));

		} catch (Exception e)
		{
			System.out.println("File can't be created");
		}
		writer.print(Huffman.decodeHuffman(file));
		writer.close();
	}

}
