package com.nagarro.entities;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPDFExporter {
	
	private List<Employee> listUsers;

	public UserPDFExporter(List<Employee> listUsers) {
		this.listUsers = listUsers;
	}

	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Employee Code",font));
		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Employee Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Location",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Date of Birth",font));
		table.addCell(cell);
		
		
	}
	
	private void writeTableData(PdfPTable table)
	{
		for(Employee emp : listUsers)
		{
			table.addCell(String.valueOf(emp.getEmployeeCode()));
			table.addCell(emp.getEmployeeName());
			table.addCell(emp.getLocation());
			table.addCell(emp.getEmail());
			table.addCell(emp.getDob());
		}
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException
	{
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLUE);
		font.setSize(18);
		
		Paragraph title=new Paragraph("List of all Employees",font);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		
		PdfPTable table=new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.5f, 3.0f});
		
		
		writeTableHeader(table);
		writeTableData(table);
		
		document.add(table);
		
		document.close();
	}

}
