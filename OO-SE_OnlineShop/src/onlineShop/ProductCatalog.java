package onlineShop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparators.ProductNumberComparator;

public class ProductCatalog {

	List<Products> myItemListing = new ArrayList<Products>();

	public ProductCatalog() {

	}

	public void addProductToListing(Products myProduct) {
		myItemListing.add(myProduct);
	}

	public void printListing() {
		ProductNumberComparator numComparator = new ProductNumberComparator();
		Collections.sort(myItemListing, numComparator);
		String outputText = String.format("%-10s %-20s %-10s %-15s %-15s", "Nummer", "Name", "Anzahl", "Preis",
				"Rabatt");
		System.out.println(outputText);
		System.out.println("------------------------------------------------------------------------");
		for (Products myProducts : myItemListing) {
			System.out.println(myProducts.toStringListing());
		}
	}

}
