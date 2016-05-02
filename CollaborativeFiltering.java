import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.IOException;

class CollaborativeFiltering {

  public static void main(String args[]) {
    try {
      String favoritesFilePath = args[0];
      // ファイルを読み込む
      FileReader fr = new FileReader(favoritesFilePath);
      BufferedReader br = new BufferedReader(fr);

      // 読み込んだファイルを1行ずつ処理する
      String line;
      StringTokenizer token;
      Integer count = 0;
      ArrayList<Content> contents = new ArrayList<Content>();
      while((line = br.readLine()) != null) {
        // 1行の文字列をカンマで分割
        token = new StringTokenizer(line, ",");
        ArrayList<Integer> tmpList = new ArrayList<Integer>();

        // 分割した文字を画面出力する
        while (token.hasMoreTokens()) {
          String str = token.nextToken();
          tmpList.add(Integer.parseInt(str));
        }
        Content content = new Content("content" + Integer.toString(count), tmpList);
        contents.add(content);
        //System.out.println(contents);
        count++;
      } 
      br.close();

      // 類似度の計算
      CollerationCalculator calculator = new PiasonCollerationCalculator();
      for (int i = 0; i < contents.size(); i++) {
        for (int j = 0; j < contents.size(); j++) {
          float result = calculator.calculate(contents.get(i), contents.get(j));
          System.out.println(Integer.toString(i) + "|" + Integer.toString(j) + "|" +result);
        }
      }

    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}

class Content {
  public String name;
  public ArrayList<Integer> userIds;

  Content(String name, ArrayList<Integer> userIds) {
    this.name = name;
    this.userIds = userIds;
  }

  public int favoriteCount() {
    return userIds.size();
  }

  public float average(int total) {
    return (float) favoriteCount() / (float) total;
  }
}

interface CollerationCalculator {
  float calculate(Content content1, Content content2);
}

class PiasonCollerationCalculator implements CollerationCalculator {
  public float calculate(Content content1, Content content2) {
    ArrayList<Integer> unionItems = union(content1.userIds, content2.userIds);
    ArrayList<Integer> commonItem = intersection(content1.userIds, content2.userIds);
    int unionItemCount = unionItems.size();
    int commonItemCount = commonItem.size();
    if (unionItemCount == 0) {
      return 0;
    }

    return (float) commonItemCount / (float) unionItemCount;
  }

  public ArrayList<Integer> intersection(ArrayList<Integer> ints1, ArrayList<Integer> ints2) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for ( Integer i: ints1) {
      if (ints2.contains(i)) result.add(i);
    }
    return result;
  }

  public ArrayList<Integer> union(ArrayList<Integer> ints1, ArrayList<Integer> ints2) {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for ( Integer i: ints1) {
      result.add(i);
    }
    for ( Integer i: ints2) {
      if (!result.contains(i)) {
        result.add(i);
      }
    }
    return result;
  }
}
