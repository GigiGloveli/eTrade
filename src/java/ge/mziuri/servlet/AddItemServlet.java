package ge.mziuri.servlet;

import ge.mziuri.model.Item;
import ge.mziuri.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import javax.servlet.http.Cookie;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

@WebServlet(name = "UAServlet", urlPatterns = {"/UAServlet"})
public class AddItemServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50000 * 1024;
    private int maxMemSize = 50000 * 1024;
    private File file;

    private String imagePath;

    public void init() {

        filePath = getServletContext().getInitParameter("file-upload");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Condition = request.getParameter("Condition");
        String type = request.getParameter("Types");
        String photo1 = request.getParameter("photo1");
        String photo2 = request.getParameter("photo2");
        String photo3 = request.getParameter("photo3");
        String point = request.getParameter("Point");
        String Name = request.getParameter("name");
        List<String> photos = new ArrayList<>();
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo3);
        Item item = new Item();
        item.setPoint(0);
        item.setName(Name);
        item.setPoint(Integer.parseInt(point));
        item.setPhotoes(photos);
        User user = new User();
        // VVX  int Rnd = item.getPoint() + (100 % 3) + 3 % 4 - 32;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("logedInUserId")) {
                user.setId(Integer.parseInt(cookie.getValue()));
            }
        }
        item.setUser(user);
        switch (type) {
            case "ავეჯი":
                item.setPoint(item.getPoint() + 32);
                break;
            case "ტექნიკა":
                item.setPoint(item.getPoint() + 29);
                break;
            case "მანქანა":
                item.setPoint(item.getPoint() + 52);
                break;
            case "მიწა":
                item.setPoint(item.getPoint() + 61);
                break;
            case "სახლი":
                item.setPoint(item.getPoint() + 70);
                break;
            case "წვრილმანი":
                item.setPoint(item.getPoint() + 8);
                break;

        }
        int con = Integer.parseInt(Condition);
        if (con >= 0 && con <= 10) {
            item.setPoint(item.getPoint() + 1);
        } else if (con >= 11 && con <= 20) {
            item.setPoint(item.getPoint() + 4);
        } else if (con >= 21 && con <= 30) {
            item.setPoint(item.getPoint() + 6);
        } else if (con >= 31 && con <= 40) {
            item.setPoint(item.getPoint() + 11);
        } else if (con >= 41 && con <= 50) {
            item.setPoint(item.getPoint() + 15);
        } else if (con >= 51 && con <= 60) {
            item.setPoint(item.getPoint() + 19);
        } else if (con >= 61 && con <= 70) {
            item.setPoint(item.getPoint() + 22);
        } else if (con >= 71 && con <= 80) {
            item.setPoint(item.getPoint() + 27);
        } else if (con >= 81 && con <= 90) {
            item.setPoint(item.getPoint() + 30);
        } else if (con >= 91 && con <= 100) {
            item.setPoint(item.getPoint() + 39);
        }
        try {
            isMultipart = ServletFileUpload.isMultipartContent(request);
            response.setContentType("text/html");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // maximum size that will be stored in memory
            factory.setSizeThreshold(maxMemSize);
            // Location to save data that is larger than maxMemSize.
            factory.setRepository(new File("c:\\temp"));
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // maximum file size to be uploaded.
            upload.setSizeMax(maxFileSize);
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            Random random = new Random();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    String fieldName = fi.getName() + " " + fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    String extention = FilenameUtils.getExtension(fileName);
                    String imageName = FilenameUtils.removeExtension(fileName);
                    if (fileName.lastIndexOf("\\") >= 0) {
                        fileName = imageName + random.nextInt() + "." + extention;
                        imagePath = fileName;
                        file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        fileName = imageName + random.nextInt() + "." + extention;
                        imagePath = fileName;
                        file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    try {
                        fi.write(file);
                    } catch (Exception ex) {
                    }
                }
            }
            Hashtable table = new Hashtable();
            Iterator<FileItem> iter = fileItems.iterator();
            while (iter.hasNext()) {
                FileItem item1 = iter.next();
                if (!item1.getFieldName().equals("file")) {
                    table.put(item1.getFieldName(), item1.getString());
                }
            }
            processRequest(request, response);
        } catch (FileUploadException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
