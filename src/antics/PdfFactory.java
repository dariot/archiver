package antics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.PrintService;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;


import com.itextpdf.text.Chunk;

import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import dto.Entity;
import dto.Picture;

@SuppressWarnings("deprecation")
public class PdfFactory {
	
	private static String WORDDOC = "WORD";
	private static String PDFDOC = "PDF";
	private static String TEXTDOC = "TXT";
	private static String EXCELDOC = "EXCEL";
	private static String PPDOC = "POWERPOINT";
	
	
    /**
     * Prints the bill associated with the provided booking on a printer.
     *
     * @param booking The booking to print the bill for.
     *
     * @throws IOException
     * @throws PrinterException
     */
    public static void printPDFFile(String path) throws IOException, PrinterException {
    	PDDocument document = PDDocument.load(new File(path));
    				
    	PrinterJob job = PrinterJob.getPrinterJob();
    	job.setPrintService(choosePrinter());
        job.setPageable(new PDFPageable(document));
        job.print();
        document.close();
        
    }
	
    /**
     * Prompts the user to choose a printer to print from, using a standard dialog box.
     *
     * The user is also able to selected from other properties such as the number of copies to
     * print, collation, etc., independently of our software.
     *
     * @return The PrintService (a.k.a the printer) selected by the user.
     */
    public static PrintService choosePrinter() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(printJob.printDialog()) {
            return printJob.getPrintService();
        }
        else {
            return null;
        }
    }
	
	/**
	 * Get the type of a file based on its extension
	 * 
	 * @param name the name of the file 
	 * @return The type of the file
	 */
	
	public static String classifyDocument(String name) {
		String[] splittedName = name.split("\\.");
		String extension = splittedName[1].toLowerCase();
		
		if(extension.startsWith("xls")) {
			return EXCELDOC;
		}else if (extension.startsWith("doc")) {
			return WORDDOC;
		}else if (extension.startsWith("ppt")) {
			return PPDOC;
		}else if (extension.equalsIgnoreCase("txt")) {
			return TEXTDOC;
		}else if (extension.equalsIgnoreCase("pdf")) {
			return PDFDOC;
		}else {
			return "";
		}
	}
	
	/**
	 * Get the converted pdf document from doc
	 * 
	 * @param data the Data of the file to be converted
	 * @param name the name of the file to be converted
	 * @return The path of the converted pdf file
	 */
	
	public static String getPDFFromDocument(byte[] data, String name) {
		String path = "";
		try {
			String docType = classifyDocument(name);
			if(docType.equalsIgnoreCase(WORDDOC)) {
				path = convertDocToPDF(data, name);
			}else if(docType.equalsIgnoreCase(PDFDOC)) {
				path = convertPDF(data, name);
			}else if(docType.equalsIgnoreCase(TEXTDOC)) {
				path = convertTxtToPDF(data, name);
			}else if(docType.equalsIgnoreCase(EXCELDOC)) {
				path = convertExcelToPDF(data, name);
			}else if(docType.equalsIgnoreCase(PPDOC)) {
				path = convertPPTToPDF(data, name);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	
	
	private static String convertPDF(byte[] data, String name) {
		String pdfPath = "";
		try{
			String pdfName = Antics.FILE_PATH +changeFileExtensionToPdf(name);
			File someFile = new File(pdfName);
	        FileOutputStream fos = new FileOutputStream(someFile);
	        fos.write(data);
	        fos.flush();
	        fos.close(); 
	        pdfPath = pdfName;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pdfPath;

	}

	/**
	 * Convert the txt file to pdf using itextpdf library
	 * 
	 * @param data the Data of the txt file to be converted
	 * @param name the name of the tt file to be converted
	 * @return The path of the converted pdf file
	 */	
	
	public static String convertTxtToPDF(byte[] data, String name) throws IOException {
		
		String pdfPath = "";
		PDDocument doc = null;
		try{
			String pdfName = Antics.FILE_PATH +changeFileExtensionToPdf(name);
		    doc = new PDDocument();
		    PDPage page = new PDPage();
		    doc.addPage(page);
		    PDPageContentStream contentStream = new PDPageContentStream(doc, page);

		    PDFont pdfFont = PDType1Font.HELVETICA;
		    float fontSize = 11;
		    float leading = 1.5f * fontSize;

		    PDRectangle mediabox = page.getMediaBox();
		    float margin = 72;
		    float width = mediabox.getWidth() - 2*margin;
		    float startX = mediabox.getLowerLeftX() + margin;
		    float startY = mediabox.getUpperRightY() - margin;

		    List<String> lines = new ArrayList<String>();
			
			InputStream is = new ByteArrayInputStream(data);	    
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String text;
			
			while ((text = br.readLine()) != null){
				int lastSpace = -1;
				while (text.length() > 0){
					int spaceIndex = text.indexOf(' ', lastSpace + 1);
					if (spaceIndex < 0){
						spaceIndex = text.length();
					}
					String subString = text.substring(0, spaceIndex);
					float size = fontSize * pdfFont.getStringWidth(subString) / 1000;

					if (size > width){
						if (lastSpace < 0)
							lastSpace = spaceIndex;
						subString = text.substring(0, lastSpace);
						lines.add(subString);
						text = text.substring(lastSpace).trim();
						lastSpace = -1;
					}else if (spaceIndex == text.length()){
						lines.add(text);
						text = "";
					}else{
						lastSpace = spaceIndex;
					}
				}
			}

		    contentStream.beginText();
		    contentStream.setFont(pdfFont, fontSize);
		    contentStream.newLineAtOffset(startX, startY);
		    for(String line: lines){		    	
		        contentStream.drawString(line);
		        contentStream.newLineAtOffset(0, -leading);
		    }
		    contentStream.endText(); 
		    contentStream.close();

		    doc.save(pdfName);
			pdfPath = pdfName;
		}finally{
		    if (doc != null){
		        doc.close();
		    }
		}
		return pdfPath;
    }
	
	/**
	 * Convert the ppt file to pdf using itextpdf library
	 * 
	 * @param data the Data of the ppt file to be converted
	 * @param name the name of the ppt file to be converted
	 * @return The path of the converted pdf file
	 */	
	
	public static String convertPPTToPDF(byte[] data, String name) throws Exception {
		String pdfPath = "";
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
		    double zoom = 2;
		    String extension = FilenameUtils.getExtension(name);
		    String pdfName = Antics.FILE_PATH +changeFileExtensionToPdf(name);
		    AffineTransform at = new AffineTransform();
		    at.setToScale(zoom, zoom);
		    com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document();
		    com.itextpdf.text.pdf.PdfWriter pdfWriter = com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfName));
		    com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(1);
		    pdfWriter.open();
		    pdfDocument.open();
		    Dimension pgsize = null;
		    com.itextpdf.text.Image slideImage = null;
		    BufferedImage img = null;
		    XMLSlideShow ppt = new XMLSlideShow(inputStream);
		    if (extension.equalsIgnoreCase("ppt")) {		    	
		        pgsize = ppt.getPageSize();
		        XSLFSlide[] slide = ppt.getSlides();
		        pdfDocument.setPageSize(new com.itextpdf.text.Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
		        pdfWriter.open();
		        pdfDocument.open();
		        for (int i = 0; i < slide.length; i++) {
		            img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
		            Graphics2D graphics = img.createGraphics();
		            graphics.setTransform(at);
	
		            graphics.setPaint(Color.white);
		            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
		            slide[i].draw(graphics);
		            graphics.getPaint();
		            slideImage = com.itextpdf.text.Image.getInstance(img, null);
		            table.addCell(new com.itextpdf.text.pdf.PdfPCell(slideImage, true));
		        }
		    }
		    if (extension.equalsIgnoreCase("pptx")) {		        
		        pgsize = ppt.getPageSize();
		        XSLFSlide[] slide = ppt.getSlides();
		        pdfDocument.setPageSize(new com.itextpdf.text.Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
		        pdfWriter.open();
		        pdfDocument.open();
		        for (int i = 0; i < slide.length; i++) {
		            img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
		            Graphics2D graphics = img.createGraphics();
		            graphics.setTransform(at);
	
		            graphics.setPaint(Color.white);
		            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
		            slide[i].draw(graphics);
		            graphics.getPaint();
		            slideImage = com.itextpdf.text.Image.getInstance(img, null);
		            table.addCell(new com.itextpdf.text.pdf.PdfPCell(slideImage, true));
		        }
		    }
		    pdfDocument.add(table);
		    pdfDocument.close();
		    pdfWriter.close();
		    pdfPath = pdfName;
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pdfPath;
	}
	
	/**
	 * Convert the excel file to pdf using lowagie library
	 * 
	 * @param data the Data of the excel file to be converted
	 * @param name the name of the excel file to be converted
	 * @return The path of the converted pdf file
	 */	
	
	public static String convertExcelToPDF(byte[] data, String name) {
		String pdfPath = "";
		try {
			
			String tmpFile = Antics.FILE_PATH+"tempor_"+name;
			File someFile = new File(tmpFile);
	        FileOutputStream fos = new FileOutputStream(someFile);
	        fos.write(data);
	        fos.flush();
	        fos.close(); 
	        
	        FileInputStream input_document = new FileInputStream(tmpFile);
			String pdfName = Antics.FILE_PATH +changeFileExtensionToPdf(name);
			XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document); 
            // Read worksheet into HSSFSheet
			XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0); 
            // To iterate over the rows
            Iterator<Row> rowIterator = my_worksheet.iterator();
            //We will create output PDF document objects at this point
            Document iText_xls_2_pdf = new Document();
            PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(pdfName));
            iText_xls_2_pdf.open();
            //we have two columns in the Excel sheet, so we create a PDF table with two columns
            //Note: There are ways to make this dynamic in nature, if you want to.
            PdfPTable my_table = new PdfPTable(2);
            //We will use the object below to dynamically add new data to the table
            PdfPCell table_cell;
            //Loop through rows.
            while(rowIterator.hasNext()) {
                    Row row = rowIterator.next(); 
                    Iterator<Cell> cellIterator = row.cellIterator();
                            while(cellIterator.hasNext()) {
                                    Cell cell = cellIterator.next(); //Fetch CELL
                                    switch(cell.getCellType()) { //Identify CELL type
                                            //you need to add more code here based on
                                            //your requirement / transformations
                                    case Cell.CELL_TYPE_STRING:
                                            //Push the data from Excel to PDF Cell
                                             table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
                                             //feel free to move the code below to suit to your needs
                                             my_table.addCell(table_cell);
                                            break;
                                    }
                                    //next line
                            }

            }
            //Finally add the table to PDF document
            iText_xls_2_pdf.add(my_table);                       
            iText_xls_2_pdf.close();              
            my_xls_workbook.close();
            input_document.close();

			pdfPath = pdfName;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pdfPath;
	}

	
	/**
	 * Convert the doc file to pdf using jodconverter library
	 * 
	 * @param data the Data of the doc file to be converted
	 * @param name the name of the doc file to be converted
	 * @return The path of the converted pdf file
	 */	
	
	public static String convertDocToPDF(byte[] data, String name) {
		
		String pdfPath = "";
		try {
			String tmpFile = Antics.FILE_PATH+"tempor_"+name;
			File someFile = new File(tmpFile);
	        FileOutputStream fos = new FileOutputStream(someFile);
	        fos.write(data);
	        fos.flush();
	        fos.close();
			
	        String ext = FilenameUtils.getExtension(name);
	        String output = "";
	        if ("docx".equalsIgnoreCase(ext)) {
	            output = readDocxFile(tmpFile);
	        } else if ("doc".equalsIgnoreCase(ext)) {
	            output = readDocFile(tmpFile);
	        } else {
	            System.out.println("INVALID FILE TYPE. ONLY .doc and .docx are permitted.");
	        }
	        pdfPath = writePdfFile(output, name);
		}catch (Exception e) {
				e.printStackTrace();
		}
		return pdfPath;		
	}

	
	public static String readDocFile(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (String para : paragraphs) {
                output = output + "\n" + para.toString() + "\n";
            }
            fis.close();
            we.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String readDocxFile(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                output = output + "\n" + para.getText() + "\n";
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String writePdfFile(String output, String name) throws Exception {
    	String pdfName = Antics.FILE_PATH +changeFileExtensionToPdf(name);
        File file = new File(pdfName);
        FileOutputStream fileout = new FileOutputStream(file);
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, fileout);
        document.open();
        String[] splitter = output.split("\\n");
        for (int i = 0; i < splitter.length; i++) {
            Chunk chunk = new Chunk(splitter[i]);
            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setStyle(com.itextpdf.text.Font.UNDERLINE);
            font.setStyle(com.itextpdf.text.Font.ITALIC);
            chunk.setFont(font);
            document.add(chunk);
            com.itextpdf.text.Paragraph paragraph = new com.itextpdf.text.Paragraph();
            paragraph.add("");
            document.add(paragraph);
        }
        document.close();
        return pdfName;
    }

	
	/**
	 * Change the file extension in a file name to .pdf
	 * 
	 * @param name The original name of the file
	 * @return The changed name
	 */	
	private static String changeFileExtensionToPdf(String name) {
		String[] splitted = name.split("\\.");
		return splitted[0]+".pdf";
	}

	/**
	 * Converts a given Picture into a PDImageXObject
	 *
	 * @param p The Picture to be converted
	 * @param document The PDDocument in use
	 * @return The converted PDImageXObject
	 */	
	public static PDImageXObject getPDImageFromPicture(Picture picture, PDDocument document) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(picture.getData());
	    BufferedImage bim = ImageIO.read(bais);
	    
	    Dimension pdfPageDim = new Dimension((int) PDRectangle.A4.getWidth()-150, (int) PDRectangle.A4.getHeight()-200);
	    Dimension imageDim = new Dimension(bim.getWidth(), bim.getHeight());
	    
	    Dimension newDim = getScaledDimension(imageDim, pdfPageDim);
	    int w = (int) newDim.getWidth();
	    int h = (int) newDim.getHeight();
	    
	    BufferedImage newimg = new BufferedImage(w, h, bim.getType());
	    Graphics2D g = newimg.createGraphics();
	    g.setComposite(AlphaComposite.Src);

	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.drawImage(bim, 0, 0, w, h, null);
	    g.dispose();
	    
	    PDImageXObject pdImage = LosslessFactory.createFromImage(document, newimg);
		return pdImage;
	}
	
	
	/**
	 * Calculate the scaled measure to fit an A4 page
	 *
	 * @param original The original Dimension of the picture
	 * @param original The target Dimension of the picture
	 * 
	 * @return The scaled factor 
	 */
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	/**
	 * Converts a given Entity to a String matrix
	 *
	 * @param e The Entity to be converted
	 * @return The converted String matrix
	 */
	public static String[][] getStringArrayFromEntity (Entity e){
		String categoryName = Antics.findCategoryById(e.getCategoryId()).getName();
		String[][] content = {
			{"Categoria dell'oggetto: ", getFormattedStringToPrint(categoryName)},
			{"Autore: ", getFormattedStringToPrint(e.getAuthor())},
			{"Titolo o descrizione dell'oggetto: ", getFormattedStringToPrint(e.getTitle())},
			{"Tecnica usata: ", getFormattedStringToPrint(e.getTechnique())},
			{"Misure: ", getFormattedStringToPrint(e.getMeasures())},			
			{"Anno di acquisizione: ", getFormattedStringToPrint(e.getBuyYear())},
			{"Importo originario pagato: ", getFormattedStringToPrint(e.getPrice())},
			{"Modalita' di pagamento: ", getFormattedStringToPrint(e.getPaymentType())},
			{"Provenienza: ", getFormattedStringToPrint(e.getOriginalPlace())},
			{"Localizzazione dell'oggetto: ", getFormattedStringToPrint(e.getActualPlace())},
			{"Valore odierno: ", getFormattedStringToPrint(e.getCurrentValue())},
			{"Data valore odierno: ", getFormattedStringToPrint(e.getCurrentValueDate())},
			{"Oggetto venduto: ", getFormattedStringToPrint(e.getSold())},			
			{"Annotazioni varie: ", getFormattedStringToPrint(e.getNotes())}	
		};
		return content;
	} 

	/**
	 * Format a string for the table
	 *
	 * @param s The String to be formatted
	 * @return The formatted String 
	 */
	public static String getFormattedStringToPrint (String s){
		if(s!= null && !s.equalsIgnoreCase("")){
			return s;
		}else{
			return " - ";
		}
	}

	/**
	 * Draw a 2x2 table into a blank PDF page with the content specified 
	 *
	 * @param p The PDPage on which the table has to be drawn
	 * @param contentStream The PDPageContentStream in use
	 * @param y The top offset express in float
	 * @param margin The float margin of the page
	 * @param content The String matrix that contains the content of the table
	 * 
	 */
	public static void drawTable(PDPage page, PDPageContentStream contentStream, float y, float margin, String[][] content) throws IOException {
		final int rows = content.length;
		final int cols = content[0].length;
		final float rowHeight = 20f;
		final float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
		final float tableHeight = rowHeight * rows;
		final float colWidth = tableWidth / (float) cols;
		final float cellMargin = 5f;
	
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			contentStream.moveTo(margin, nexty);
			contentStream.lineTo(margin + tableWidth, nexty);
			nexty -= rowHeight;
		}
	
		// draw the columns
		float nextx = margin;
		for (int i = 0; i <= cols; i++) {
			contentStream.moveTo(nextx, y);
			contentStream.lineTo(nextx, y - tableHeight);
			nextx += colWidth;
		}
	
		// now add the text
		contentStream.setFont(PDType1Font.HELVETICA, 11);
	
		float textx = margin + cellMargin;
		float texty = y - 15;
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[i].length; j++) {
				String text = content[i][j];
				contentStream.beginText();

					contentStream.setNonStrokingColor(Color.BLACK);

					contentStream.newLineAtOffset(textx, texty);
					contentStream. showText(text);
				contentStream.endText();
				textx += colWidth;
			}
			texty -= rowHeight;
			textx = margin + cellMargin;
		}
	}

}
