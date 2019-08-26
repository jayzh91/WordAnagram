
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
    public class Solution {
        /*
        Use Trie as the data structure to store all words,
        due to it's low time complexity: search and insert are O(k),
        k is the length of word.
         */
        public static class TrieNode {
            private boolean isEnd;
            private HashMap<Character, TrieNode> children; // use a Hashmap to store all child nodes
            public TrieNode() {
                children = new HashMap<>();
                isEnd = false;
            }
            public HashMap<Character, TrieNode> getChildren(){return children;}
            public void setEnd(boolean val) {
                isEnd = val;
            }
            public boolean isEnd() {
                return isEnd;
            }

        }
        /*
        Insert function insert a word to Trie.
         */
        public static void insert(String word) {
            if (word == null || word.length() == 0) return;
            int len = word.length();
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                HashMap<Character, TrieNode> children = cur.getChildren();
                if (!children.containsKey(c)) {
                    children.put(c, new TrieNode());
                }
                cur = children.get(c);
            }
            cur.setEnd(true);
        }
        /*
        IsWord function return true or false if a string is stored as a word in the Trie.
         */
        public static boolean isWord(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                HashMap<Character, TrieNode> children = cur.getChildren();
                if (!children.containsKey(c)) return false;
                cur = children.get(c);
            }
            return cur.isEnd();
        }
        /*
        NotInTrie function return true of false if the prefix of a string is stored in trie.
         */
        public static boolean notInTrie(String prefix) {
            TrieNode cur = root;
            for (char c : prefix.toCharArray()) {
                HashMap<Character, TrieNode> children = cur.getChildren();
                if (!children.containsKey(c)) return true;
                cur = children.get(c);
            }
            return false;
        }
        /*
        FindAllAnagram function return a list of strings that all anagrams (stored in Trie) of a list of characters,
        no matter the anagram is word or not. Use backtracking approach to go through all combinations.
         */

        public static List<String> findAllAnagram(List<Character> list) {
            List<String> res = new ArrayList<>();
            Collections.sort(list); // sort only when list of characters contains duplicates
            findAllAnagramHelper( list,  res, "", new boolean[list.size()]);
            return res;
        }
        public static void findAllAnagramHelper(List<Character> list, List<String> res, String str, boolean[] used) {
            if (str.length() == list.size()) {
                res.add(str);
                //capitalize the first character only when list of character is case insensitive, but return value is case sensitive. 
                // uncomment if return string is case sensitive
                // String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                // res.add(cap);
                return;
            }
            for (int i = 0; i < list.size(); i++) {
              
                if (used[i] || i > 0 && list.get(i) == list.get(i - 1) && !used[i - 1]) continue; //only when list of character contains duplicates
                str += list.get(i);
                // use notInTrie function to determine if str is in Trie, continue if not, a way of optimization
                if (notInTrie(str)) {
                    str = str.substring(0, str.length() - 1);
                    continue;
                }
                used[i] = true;
                findAllAnagramHelper(list, res, str, used);
                used[i] = false;
                str = str.substring(0, str.length() - 1);
            }
        }
        /*
        Read input from a local file
         */
        public static void trie_init_localFile() {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader("/usr/share/dict/words"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String word = line.trim().toLowerCase(); //toLowerCase only when return value is case insensitive
                    insert(word);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        Read input from a website
         */
        public static void trie_init_https() {
            String url = "https://raw.githubusercontent.com/lad/words/master/words";
          try{
              HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
              con.setRequestMethod("GET");
//            System.out.println("Response code: " + con.getResponseCode());
              BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
              String line;
              while ((line = reader.readLine()) != null) {
                  String word = line.trim().toLowerCase(); //toLowerCase only when return value is case insensitive
                  insert(word);
              }
              reader.close();
          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
        }

        /*
        Print final result in the list of string
         */
        public static void print(List<String> res) {
            for (String s : res) {
                System.out.println(s);
            }
        }

        public static TrieNode root;
        public static void main(String[] args) {
            root = new TrieNode();     //initiate root
            List<String> res = new ArrayList<>(); // return a list of string
            List<Character> list = Arrays.asList('a','b','l','e','r'); // input as list of characters
//          trie_init_localFile();      // use local file as input
            trie_init_https();          // use website request as input
            List<String> anagrams = findAllAnagram(list); // find all anagrams from the list of characters
            for (String anagram : anagrams) {
                if (isWord(anagram)) res.add(anagram); // check if the anagram is word, add word to the final result
            }
            print(res);     // print final result
        }
    }
