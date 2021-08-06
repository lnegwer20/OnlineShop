package onlineShop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import comparators.ProductNumberComparator;
import comparators.ProductPriceComparatorAscending;
import comparators.ProductPriceComparatorDescending;

public class ShoppingCart {

	private List<Products> myShoppingCart = new ArrayList<Products>();
	private Double totalPrice = 0.0;
	private Products returnProduct;
	private static Font timesFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	private static Font timesFontSmall = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
	private static Font timesFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);

	public ShoppingCart() {

	}

	public void addProductToCart(Products product, int amount) {

		int amountBefore = product.getQuantity();

		if (product.getQuantity() < amount) {
			System.out.println("Fehler Bestand");
		} else {
			product.setQuantity(product.getQuantity() - amount);
			myShoppingCart.add(new Products(product.getProductNumber(), product.getProductName(),
					amountBefore - product.getQuantity(), product.getBasePrice()));
		}
	}

	public void printProducts() {
		String outputText = String.format("%-10s %-10s %-20s %-10s %-15s", "Position", "Nummer", "Name", "Anzahl",
				"Preis");
		System.out.println(outputText);
		System.out.println("-------------------------------------------------------------");
		for (Products myProducts : myShoppingCart) {
			System.out.println(myShoppingCart.indexOf(myProducts) + "           " + myProducts.toString());
			totalPrice = totalPrice + (myProducts.getQuantity() * myProducts.getBasePrice());
		}
		System.out.println("-------------");
		System.out.println("Summe: " + totalPrice);
		totalPrice = 0.0;
	}

	public double getTotalCost() {
		for (Products myProducts : myShoppingCart) {
			totalPrice = totalPrice + (myProducts.getQuantity() * myProducts.getBasePrice());
		}
		return totalPrice;
	}

	// Produktnummer
	public void sortAfterNumber() {
		ProductNumberComparator numComparator = new ProductNumberComparator();
		Collections.sort(myShoppingCart, numComparator);
	}

	// Absteigend
	public void sortAfterPriceDesc() {
		ProductPriceComparatorDescending descComparator = new ProductPriceComparatorDescending();
		Collections.sort(myShoppingCart, descComparator);
	}

	// Aufsteigend
	public void sortAfterPriceAsc() {
		ProductPriceComparatorAscending ascComparator = new ProductPriceComparatorAscending();
		Collections.sort(myShoppingCart, ascComparator);
	}

	public int getTotalItems() {
		return myShoppingCart.size();
	}

	// Get-Index
	public Products get(int pos) {
		returnProduct = myShoppingCart.get(pos);
		return returnProduct;
	}

	public void createPDFBank(String adresse, String bank, long BankCode, long accountNumber) {

		try {

			float docWidth;
			String pdf_path = System.getProperty("user.home") + "\\Desktop\\Quittung.pdf";
			Image my_img = Image.getInstance(System.getProperty("user.home") + "\\Desktop\\shopbild.png");
			Document myDoc = new Document();

			docWidth = myDoc.getPageSize().getWidth() - myDoc.leftMargin() - myDoc.rightMargin();

			my_img.scaleAbsolute(docWidth, 150);

			PdfWriter.getInstance(myDoc, new FileOutputStream(pdf_path));

			myDoc.open();

			myDoc.add(my_img);
			myDoc.add(new Paragraph("Vielen Dank f�r deinen Einkauf in unserem Shop!\n", timesFont));
			myDoc.add(new Paragraph("Hier ist eine Zusammenfassung deines Einkaufs: \n\n", timesFontSmall));

			float[] columnWidths = { 6, 6, 3, 5 };

			PdfPTable myTable = new PdfPTable(columnWidths);
			PdfPCell c1 = new PdfPCell(new Paragraph("Produktnummer", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Produktname", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Anzahl", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Preis pro St�ck", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			for (Products myProducts : myShoppingCart) {
				PdfPCell e1 = new PdfPCell(
						new Paragraph(String.format("%d", myProducts.getProductNumber()), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(myProducts.getProductName(), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(String.format("%d", myProducts.getQuantity()), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(myProducts.getBasePrice() + " �", timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);
			}
			myDoc.add(myTable);

			myDoc.add(new Paragraph("\nDie Lieferung und Abbuchung erfolgt auf folgende Daten:\n", timesFont));
			myDoc.add(new Paragraph("\nAdresse: " + adresse, timesFontSmall));
			myDoc.add(new Paragraph("Bank: " + bank, timesFontSmall));
			myDoc.add(new Paragraph("Banknummer: " + BankCode, timesFontSmall));
			myDoc.add(new Paragraph("Kontonummer: " + accountNumber, timesFontSmall));

			myDoc.close();

		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Fehler PDF-Erstellung");
		}
	}

	public void createPDFCard(String adresse, long cardNumber, int cvv) {
		try {

			float docWidth;
			String pdf_path = System.getProperty("user.home") + "\\Desktop\\Quittung.pdf";
			Image my_img = Image.getInstance(System.getProperty("user.home") + "\\Desktop\\shopbild.png");
			Document myDoc = new Document();

			docWidth = myDoc.getPageSize().getWidth() - myDoc.leftMargin() - myDoc.rightMargin();

			my_img.scaleAbsolute(docWidth, 150);

			PdfWriter.getInstance(myDoc, new FileOutputStream(pdf_path));

			myDoc.open();

			myDoc.add(my_img);
			myDoc.add(new Paragraph("Vielen Dank f�r deinen Einkauf in unserem Shop!\n", timesFont));
			myDoc.add(new Paragraph("Hier ist eine Zusammenfassung deines Einkaufs: \n\n", timesFontSmall));

			float[] columnWidths = { 6, 6, 3, 5 };

			PdfPTable myTable = new PdfPTable(columnWidths);
			PdfPCell c1 = new PdfPCell(new Paragraph("Produktnummer", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Produktname", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Anzahl", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			c1 = new PdfPCell(new Paragraph("Preis pro St�ck", timesFontBold));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setVerticalAlignment(Element.ALIGN_CENTER);
			myTable.addCell(c1);

			for (Products myProducts : myShoppingCart) {
				PdfPCell e1 = new PdfPCell(
						new Paragraph(String.format("%d", myProducts.getProductNumber()), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(myProducts.getProductName(), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(String.format("%d", myProducts.getQuantity()), timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);

				e1 = new PdfPCell(new Paragraph(myProducts.getBasePrice() + " �", timesFontSmall));
				e1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setVerticalAlignment(Element.ALIGN_CENTER);
				myTable.addCell(e1);
			}
			myDoc.add(myTable);

			myDoc.add(new Paragraph("\nDie Lieferung und Abbuchung erfolgt auf folgende Daten:\n", timesFont));
			myDoc.add(new Paragraph("\nAdresse: " + adresse, timesFontSmall));
			myDoc.add(new Paragraph("Kartennummer: " + cardNumber, timesFontSmall));
			myDoc.add(new Paragraph("CVV-Code: " + cvv, timesFontSmall));

			myDoc.close();

		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Fehler PDF-Erstellung");
		}

	}
}