package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {

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
	
	
	public int login(String userID, String userPassword) {
		
		try {
			getCon();
			
			sql = "select userPassword from user where userID=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userID);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {  
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인 성공
				}else {
					return 0; //비밀번호 불일치
				}
			}
			return -1; //아이디가 없음
			
		} catch (Exception e) {
			
		}finally {
			freeResource();
		}
		
		return -2; //DB오류
		
	}//login();
	
	public int join(User user) {
		
		
		try {
			getCon();
			
			sql = "insert into user values(?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("join메서드 내부에서 오류: " + e.getMessage());
		}finally {
			freeResource();
		}
		
		return -1; //데이터베이스 오류
	}
	
}
