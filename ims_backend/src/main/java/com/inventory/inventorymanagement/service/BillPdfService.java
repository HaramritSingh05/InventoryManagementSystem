package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.dto.BillDTO;
import com.electrowaveselectronics.inventorymanagement.dto.ProductDTO;
import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.entity.DeliveryOrder;
import com.electrowaveselectronics.inventorymanagement.repository.DeliveryRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class BillPdfService {

    @Autowired
    DeliveryRepository deliveryRepository;
    private Logger logger = LoggerFactory.getLogger(BillPdfService.class);
    public ByteArrayInputStream createPdf(int orderId){
        logger.info("Create PDF Started");



        ByteArrayOutputStream out = new ByteArrayOutputStream();


        try {
            DeliveryOrder deliveryOrder = deliveryRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
            if (orderId <= 0) {
                throw new IllegalArgumentException("Invalid order ID");
            }
            ////////////  DATA //////////////////
            Customer customer = deliveryOrder.getCustomer();
            List<ProductDTO> productDTOList = deliveryOrder.getProducts();

            String customerAddress = customer.getCustomerAddress();
            String customerName = customer.getCustomerName();
            int invoiceNumber = orderId;
            int totalQuantity = deliveryOrder.getOrderQuantity();
            Date orderDate = deliveryOrder.getOrderDate();
            int totalAmount =  deliveryOrder.getTotalSellPrice();
            String sellerName = deliveryOrder.getGodownHeadName();
            String godownAddress = deliveryOrder.getGodownAddress();
            ///////////////////////////////////////////////////
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            PdfWriter.getInstance(document, out);
            document.open();
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
            PdfPCell titleCell = new PdfPCell(new Paragraph("TAX Invoice", titleFont));
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            // Image
            Image img = Image.getInstance("src/main/resources/assets/pyramidLogo.png"); // Replace "path_to_your_image.jpg" with the actual path to your image file
            img.scaleToFit(100, 100); // Adjust the image size as needed
            PdfPCell imageCell = new PdfPCell(img);
            imageCell.setBorder(Rectangle.NO_BORDER);


            PdfPTable titleImageTable = new PdfPTable(2);
            titleImageTable.setWidthPercentage(100);
            titleImageTable.setWidths(new float[]{1, 1});
            titleImageTable.addCell(imageCell);
            titleImageTable.addCell(titleCell);


            titleImageTable.setSpacingAfter(20f);
            document.add(titleImageTable);


            // Border for the bill section
            Rectangle border = new Rectangle(document.left()-10, document.top(), document.right()+10, document.bottom() + 100);
            border.setBorder(Rectangle.BOX);
            border.setBorderColor(Color.BLACK);
            border.setBorderWidth(1f);
            document.add(border);

            // Seller Details
            Font sellerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph sellerPara = new Paragraph();
            sellerPara.setFont(sellerFont);
            sellerPara.add(new Chunk("Sold By"+ "\n", totalFont));
            sellerPara.add(sellerName + "\n");
            sellerPara.add(godownAddress + "\n");
            sellerPara.add("Order Id : INV-"+invoiceNumber + "\n");
            sellerPara.add("Invoice Date: " + orderDate.toString().substring(0, 10) + "\n");
            sellerPara.setSpacingAfter(10f);
            document.add(sellerPara);

            // Customer Details
            Font customerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph customerPara = new Paragraph();
            customerPara.setFont(customerFont);
            customerPara.add(new Chunk("Invoice To"+ "\n",totalFont));
            customerPara.add("Name : "+ customerName + "\n");
            customerPara.add("Address : "+ customerAddress + "\n");

            customerPara.setSpacingAfter(10f);
            document.add(customerPara);

            // Invoice Items
            Font itemFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            PdfPTable table = new PdfPTable(7); // 7 columns for Sr. No, Product, Rate, Qty., Taxable value, GST, Total
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Heading Row
            String[] headings = {"Sr. No", "Product", "Rate", "Qty.", "Taxable value", "GST (%)", "Total"};
            for (String heading : headings) {
                PdfPCell headingCell = new PdfPCell(new Paragraph(heading, itemFont));
                headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headingCell.setBackgroundColor(Color.LIGHT_GRAY);
                table.addCell(headingCell);
            }

            // Table Data
            int srNo = 1;
            for (ProductDTO productDTO : productDTOList) {
                table.addCell(new Paragraph(String.valueOf(srNo++), itemFont));
                table.addCell(new Paragraph(productDTO.getProductName(), itemFont));
                table.addCell(new Paragraph(String.format("%.2f", productDTO.getSellPrice()), itemFont));
                table.addCell(new Paragraph(String.valueOf(productDTO.getOrderQuantity()), itemFont));

                double taxableValue = productDTO.getSellPrice() * productDTO.getOrderQuantity() * 0.18;
                table.addCell(new Paragraph(String.format("%.2f", taxableValue), itemFont));


                table.addCell(new Paragraph("18", itemFont));

                double total = taxableValue + (productDTO.getSellPrice() * productDTO.getOrderQuantity());
                table.addCell(new Paragraph(String.format("%.2f", total), itemFont));
            }

            document.add(table);

            // Total Quantity and Total Amount
            Paragraph totalQtyPara = new Paragraph("Total Quantity: " + totalQuantity, totalFont);
            totalQtyPara.setAlignment(Element.ALIGN_LEFT);
            double totalAmountWithTax = totalAmount + (totalAmount * 0.18);

            // Format the total amount to show up to two decimal places
            String formattedTotalAmount = String.format("%.2f", totalAmountWithTax);
            Paragraph totalAmountPara = new Paragraph("Total Amount (Rs.): " + formattedTotalAmount, totalFont);


            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{1, 2});

            PdfPCell totalQtyCell = new PdfPCell(totalQtyPara);
            totalQtyCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell totalAmountCell = new PdfPCell(totalAmountPara);
            totalAmountCell.setBorder(Rectangle.NO_BORDER);
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalTable.addCell(totalQtyCell);
            totalTable.addCell(totalAmountCell);

            document.add(totalTable);

            document.close();
        } catch (DocumentException e) {
            logger.error("Error occurred: {}", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
