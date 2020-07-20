package bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BbsDAO {
	
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String sql;
	
	public void freeResource() {
		if(rs != null) {try {rs.close();}catch(SQLException e){e.printStackTrace();}}
		if(pstmt != null) {try {pstmt.close();}catch(SQLException e){e.printStackTrace();}}	
		if(con != null) {try {con.close();}catch(SQLException e){e.printStackTrace();}}
	
	}//freeResource();
	
	public void getCon() {
		try {
			
			Context init = new InitialContext();
			
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/jspbeginner");
			
			con = ds.getConnection(); 

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// getCon();
	
	public String getDate() {
		
		try {
			getCon();
			sql = "select now()";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //DB오류
		
	}//getDate();
	
	public int getNext() {
		
		try {
			getCon();
			sql = "select bbsID from bbs order by bbsID desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; //첫번째 게시글인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //DB오류
		
	}//getNext();
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		
		try {
			getCon();
			sql = "insert into bbs values(?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("write메서드 내부에서 오류: " +e.getMessage());
		}
		return -1; //DB오류
	
	}//write();

}


