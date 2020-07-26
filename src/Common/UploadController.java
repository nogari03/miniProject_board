package Common;

import Board.BoardService;
import Board.BoardServiceImpl;
import Board.BoardVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/upload")
public class UploadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doHandle(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doHandle(request, response);
    }

    private void doHandle(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        String savePath = "/Users/nogari03/Desktop";
        int fileSize = 3 * 1024 * 1024;
        String encoding = "utf-8";
        MultipartRequest multi = new MultipartRequest(request, savePath, fileSize, encoding, new DefaultFileRenamePolicy());


        String row_num = multi.getParameter("row_num");
        String article_no = multi.getParameter("article_no");
        String password = multi.getParameter("password");
        String writer_id = multi.getParameter("writer_id");
        String writer_name = multi.getParameter("writer_name");
        String title = multi.getParameter("title");
        String content = multi.getParameter("content");
        String file = multi.getFilesystemName("file");

        String command = multi.getParameter("command");
        BoardService service = new BoardServiceImpl();

        BoardVO vo;

        if("add".equals(command)) { // 글 추가
            vo = new BoardVO(row_num,article_no,writer_id,writer_name,title,password,content,file);
            service.insertBoard(vo);
            response.sendRedirect("/board");

        }else if ("update".equals(command)) { // 글 수정
            service.uploadFile(file, article_no);
            vo = new BoardVO(article_no, title, content);
            service.updateContent(vo);
            System.out.println("file upload complete");
            response.sendRedirect("/board");
        }
    }
}
