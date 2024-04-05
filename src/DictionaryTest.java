//DSA Final Project
//Dictionary implementation Using AVL Tree
//Name: Usama saleem
//Fa19-BSCS-0046

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class DictionaryTest {
	protected static String[] entries = new String[26 * 26];

	protected static void fill() {
		// Insert 26 * 26 entries
		for (int i = 0; i < 26; i++)
			for (int j = 0; j < 26; j++) {
				StringBuffer s = new StringBuffer();
				s.append((char) ((int) 'A' + i));
				s.append((char) ((int) 'A' + j));
				s.append((char) ((int) 'A' + j));
				entries[i * 26 + j] = s.toString();
			}
	}
public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		//a) Read Words.txt file and create the dictionary (i.e., AVL Tree).
		RandomAccessFile file = new RandomAccessFile("Demo.txt", "r");

		FileChannel channel = file.getChannel();

		System.out.println("File size is: " + channel.size());

		ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

		channel.read(buffer);

		buffer.flip();//Restore buffer to position 0 to read it

		System.out.println("Reading content and printing ... ");

		for (int i = 0; i < channel.size(); i++) {
			System.out.print((char) buffer.get());
		}
		//b) Insert a new word from user with all its associated data (up to three
	//meanings)
		try {
		String Word,Meaning,Antonym,Synonym;
		System.out.println("Enter the Word: ");
		Word=sc.next();
		System.out.println("Enter the Meaning: ");
		Meaning=sc.next();
			System.out.println("Enter the Antonym: ");
			Antonym=sc.next();
			System.out.println("Enter the Synonym: ");
			Synonym=sc.next();
			//c) Find a word and give the user the option to update the information of the
			//word if found.
		String textToAppend = " \n"+Word+" :"+Meaning+","+Antonym+","+Synonym+" ;"+" ";
		Path path = Paths.get("Demo.txt");
		Files.write(path, textToAppend.getBytes(), StandardOpenOption.APPEND);
		System.out.println("Successfully wrote to the file.");
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		channel.close();
		file.close();
		AVLDictionary<String, SortableString> dict2 = new AVLDictionary<String, SortableString>();

		fill();
		for (int i = 0; i < 26 * 26; i++) {
			int e;
			do {
				e = (int) (Math.random() * (26 * 26));
			} while (entries[e] == null);

			dict2.insert(new SortableString(entries[e]), entries[e]);
			entries[e] = null;
		}

		dict2.printTree();

		System.out.println("The initial AVL tree has a maximum depth of "
				+ dict2.depth());

		fill();
		for (int i = 0; i < 13 * 26; i++) {
			int e;
			do {
				e = (int) (Math.random() * (26 * 26));
			} while (entries[e] == null);

			dict2.delete(new SortableString(entries[e]));
		}

		System.out
				.println("After deletes, the AVL tree has a maximum depth of "
						+ dict2.depth());

		fill();
		for (int i = 0; i < 6 * 26; i++) {
			int e;
			do {
				e = (int) (Math.random() * (26 * 26));
			} while (entries[e] == null);

			dict2.insert(new SortableString(entries[e]), entries[e]);
		}

		System.out
				.println("After insertions, the AVL tree has a maximum depth of "
						+ dict2.depth());

		fill();
		for (int i = 0; i < 6; i++) {
			int e;
			do {
				e = (int) (Math.random() * (26 * 26));
			} while (entries[e] == null);

			System.out.print("Searching for " + entries[e] + ": ");
		if (dict2.search(new SortableString(entries[e])) == null) {
				System.out.println("not found in Dictionary.");
			} else {
				System.out.println("found in Dictionary.");

			}

		}
	System.out.println("Good :Better,NotBetter,Good ;");
	System.out.println("Usama Saleem Fa19-bscs-0046");
	}
}
