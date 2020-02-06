import java.io.*;
import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import yahoofinance.*;

public class myStock {
	HashMap<String, stockInfo> stockMap;
	TreeSet<Map.Entry<String, stockInfo>> treeBeard;
	private static class stockInfo {
		private String name;
		private BigDecimal price;
		public stockInfo(String nameIn, BigDecimal priceIn) {
			name = nameIn;
			price = priceIn;
		}
		public BigDecimal getPrice() { return price; }
		public String toString() {
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(name + " " + price.toString());
			return stockInfoString.toString();
		}
	}
	
	public myStock () {
		stockMap = new HashMap<String, stockInfo>();
		treeBeard = new TreeSet<Map.Entry<String, stockInfo>>(new priceComparator());
	}
	
	class priceComparator implements Comparator<Map.Entry<String, stockInfo>> { 

		public int compare(Entry<String, stockInfo> first, Entry<String, stockInfo> second) {
			BigDecimal x= first.getValue().getPrice();
			BigDecimal y= second.getValue().getPrice();
			
			return y.compareTo(x);
		}

	} 
	public void insertOrUpdate(String symbol, stockInfo stock) {
		stockMap.put(symbol, stock);
		treeBeard.add(Map.entry(symbol, stock));
	}
	
	public stockInfo get(String symbol) {
		if (stockMap.containsKey(symbol)) {
			stockInfo info = stockMap.get(symbol);
			return info;
		}
		return null;
	}
	
	public List<Map.Entry<String, stockInfo>> top(int k) {
		
		Iterator<Map.Entry<String, stockInfo>> setIterator = treeBeard.iterator();
		List<Map.Entry<String, stockInfo>> list = new ArrayList<Map.Entry<String, stockInfo>>();
		for (int i = 0 ; i < k; i++) {
			list.add(setIterator.next());
		}
		return list;
	}
	

    public static void main(String[] args) throws IOException {   	
    	
    	myStock techStock = new myStock();
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] var = line.split(":");

				Stock stock = YahooFinance.get(var[0]);

				if(stock.getQuote().getPrice() != null) {
					techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice())); 
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 1;
		System.out.println("===========Top 10 stocks===========");
		
		for (Map.Entry<String, stockInfo> element : techStock.top(10)) {
		    System.out.println("[" + i + "]" +element.getKey() + " " + element.getValue());
		    i++;
		}
		
		// test the get operation
		System.out.println("===========Stock info retrieval===========");
    	System.out.println("VMW" + " " + techStock.get("VMW"));
    	System.out.println("CSCO" + " " + techStock.get("CSCO"));
    }
}