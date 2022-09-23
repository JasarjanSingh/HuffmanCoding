// Jasarjan Singh
// 06/02/2022
// CSE 143
// TA: Himani Nijhawan
// Assesment Huffman Coding
// This program uses huffman coding algorithm to compress a file size
// by reducing the number of bits used for each ascii character based on 
// the frequency of that character in the file

import java.util.*;
import java.io.*;

public class HuffmanCode {
    private HuffmanNode huffmanTree;

    // post: initializes a new HuffmanCode object with the given array of frquencies
    //       frequencies is an array of frequencies where frequences[i] is the 
    //       count of the character with ASCII value i. Uses a PriorityQueue queue to 
    //       build the Huffman code.
    public HuffmanCode(int[] frequencies) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                queue.add(new HuffmanNode(i, frequencies[i]));
            }
        }
        while (queue.size() > 1) {
            HuffmanNode first = queue.remove();
            HuffmanNode second = queue.remove();
            queue.add(new HuffmanNode(-1, first.frequency + second.frequency, first, second));
        }
        huffmanTree = queue.remove();
    }

    // pre : the Scanner input is not null and is always 
    //       contains data encoded in the standard format
    // post: initializes a new HuffmanCode object by reading in a 
    //       previously constructed code from a .code file
    public HuffmanCode(Scanner input) {
        while(input.hasNextLine()) {
            int asciiValue = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            huffmanTree = buildTree(huffmanTree, asciiValue, code);
        }
    }

    // post: recursively builds a binary tree representing Huffman code. traverses through
    //       the tree until reaching a leaf node where HuffmanNodes are created until reaching 
    //       the new leaf node with the given ascii value. HuffmanNode root is the root of the 
    //       binary tree being built to store the Huffman code. int asciiValue is the current 
    //       ascii value being stored in the binary tree. String code is the huffman code for the
    //       ascii value. returns the HuffmanNode that stores the huffman code as a binary tree.
    private HuffmanNode buildTree(HuffmanNode root, int asciiValue, String code) {
        if (root == null) {
            root = new HuffmanNode(-1, 0);
        }
        if (code.length() == 1) {
            if(code.equals("0")) {
                root.left = new HuffmanNode(asciiValue, 0);
            } else {
                root.right = new HuffmanNode(asciiValue, 0);
            }
        } else {
            if (code.charAt(0) == '0') {
                root.left = buildTree(root.left, asciiValue, code.substring(1));
            } else {
                root.right = buildTree(root.right, asciiValue, code.substring(1));
            }
        }
        return root;
    }

    // post: stores the current Huffman codes to the given output stream in the standard format
    //       of ascii value on first line and huffman code on the second. 
    public void save(PrintStream output) {
        save(huffmanTree, output, "");
    }

    // post: recursively traverses through the binary tree storing the Huffman codes.
    //       stores the ascii value and corresponding huffman code in an output stream 
    //       for every ascii value in the binary tree.
    private void save(HuffmanNode root, PrintStream output, String code) {
        if(root.left == null && root.right == null) {
            output.println(root.asciiValue);
            output.println(code);
        } else {
            save(root.left, output, code + "0");
            save(root.right, output, code + "1");
        }
    }

    // pre : the input contains a legal encoding of characters for this tree's Huffman code
    // post: reads individual bits from the input and writes the corresponding 
    //       characters to the given output stream. Stops reading when the input is empty
    public void translate(Scanner input, PrintStream output) {
        while (input.hasNext()){
            translate(huffmanTree, input, output);
        }
    }

    // post: recursively traverses the Huffman tree until reaching a leaf node with
    // a character and 'translates' it to the output by writing the corresponding 
    // decompressed characters to the given output stream.
    private void translate(HuffmanNode root, Scanner input, PrintStream output) {
        if (root.left == null && root.right == null) {
            output.write(root.asciiValue);
        } else {
            char curr = input.next().charAt(0);
            if (curr == '0') {
                translate(root.left, input, output);
            } else {
                translate(root.right, input, output);
            }
        }
    }

    // post: The HuffmanNode class implements the Comparable interface and 
    //       represents each node of the huffman code binary tree
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public int asciiValue;
        public int frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        // post: constructs a new HuffmanNode object with the given ascii value, frequecy,
        //       left, and right nodes. asciiValue is the ascii value  that represents a 
        //       character for this node. frequency is the count of asciiValue. 
        //       left is the HuffmanNode object representing the next left node in the tree
        //       right is the HuffmanNode object representing the next right node in the tree
        public HuffmanNode(int asciiValue, int frequency, HuffmanNode left, HuffmanNode right) {
            this.asciiValue = asciiValue;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        // post: constructs a new HuffmanNode object with given ascii value, frequecy, and 
        //       sets left and right nodes to null
        public HuffmanNode(int asciiValue, int frequency) {
            this(asciiValue, frequency, null, null);
        }

        // post: Returns the difference between the frequency of this HuffmanNode
        //       and the frequency of the given other HuffmanNode,  lower frequencies 
        //       are considered "less" than higher frequencies
        public int compareTo(HuffmanNode other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }
}
