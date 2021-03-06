package assn2.daosImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import assn2.beans.UserBean;
import assn2.daos.UserDAO;
import assn2.database.*;
import assn2.exceptions.*;
import assn2.database.DBConnectionFactory;



public class UserDAOImpl implements UserDAO {
	
	private DBConnectionFactory services;
	
	public UserDAOImpl() {
		try {
			services = new DBConnectionFactory();
		} catch (ServiceLocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserDAOImpl(DBConnectionFactory services) {
		this.services = services;
	}

	
	//insert a new record in user table
	public void insert(UserBean bean) {
		Connection con = null;
		   try {
			 //get the connection 
			 con = services.createConnection();
			 PreparedStatement stmt = con.prepareStatement("Insert Into user (userid, fname, lname, security_level, email, username,password,address) values (?, ?, ?, ?, ?, ?,?,?)");
		     stmt.setInt(1, bean.getUserid());
		     stmt.setString(2, bean.getFname());
		     stmt.setString(3, bean.getLname());
		     stmt.setString(4, bean.getSecurity_level());
		     stmt.setString(5, bean.getEmail());
		     stmt.setString(6, bean.getUsername());
		     stmt.setString(7, bean.getPassword());
		     stmt.setString(8, bean.getAddress());
		     
		     //execute the update
		     int n = stmt.executeUpdate();
		     if (n != 1)//remember to catch the exceptions
		 	   throw new DataAccessException("Did not insert one row into database");
		   } catch (ServiceLocatorException e) {
		       throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		   } catch (SQLException e) {
		       throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		   } finally {
		      if (con != null) {
		         try {
		           con.close();//and close the connections etc
		   		 } catch (SQLException e1) {  //if not close properly
		           e1.printStackTrace();
		         }
		      }
		   }
	}
	/**
	 */
	public void insert(String fname, String lname, String security_level, String email, String username, String password,String address) {
		Connection con = null;
		   try {
			 //get the connection 
			 con = services.createConnection();
			 PreparedStatement stmt = con.prepareStatement("Insert Into user (fname, lname, security_level, email, username,password,address) values (?, ?, ?, ?, ?,?,?)");
		     stmt.setString(1, fname);
		     stmt.setString(2, lname);
		     stmt.setString(3, security_level);
		     stmt.setString(4, email);
		     stmt.setString(5, username);
		     stmt.setString(6, password);
		     stmt.setString(7, address);
		     
		     //execute the update
		     int n = stmt.executeUpdate();
		     if (n != 1)//remember to catch the exceptions
		 	   throw new DataAccessException("Did not insert one row into database");
		   } catch (ServiceLocatorException e) {
		       throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		   } catch (SQLException e) {
		       throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		   } finally {
		      if (con != null) {
		         try {
		           con.close();//and close the connections etc
		   		 } catch (SQLException e1) {  //if not close properly
		           e1.printStackTrace();
		         }
		      }
		   }
	}
	
	public UserBean get(int id) {
		Connection con = null;
		UserBean r = null;
		try {
			 //get the connection 
			 con = services.createConnection();		 
			 PreparedStatement stmt = con.prepareStatement("select * from user where userid=(?)");
		     stmt.setInt(1, id);
		     ResultSet rs = stmt.executeQuery();
			 if (rs == null)//remember to catch the exceptions
			 	  throw new DataAccessException("cannot find any record owned by that id");
			 while(rs.next()){
				r = createUserBean(rs);
			 }
		   } catch (ServiceLocatorException e) {
		       throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		   } catch (SQLException e) {
		       throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		   } finally {
		      if (con != null) {
		         try {
		           con.close();//and close the connections etc
		   		 } catch (SQLException e1) {  //if not close properly
		           e1.printStackTrace();
		         }
		   }	      
		}
		return r; //the last
	}
	//because username is unique
	public int getId(String username) {
		Connection con = null;
		int r = 0;
		try {
			 //get the connection 
			 con = services.createConnection();		 
			 PreparedStatement stmt = con.prepareStatement("select * from user where username=(?)");
		     stmt.setString(1, username);
		     ResultSet rs = stmt.executeQuery();
			 if (rs == null)//remember to catch the exceptions
			 	  throw new DataAccessException("cannot find any record owned by that id");
			 rs.next();
			 r = rs.getInt("userid");
		   } catch (ServiceLocatorException e) {
		       throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		   } catch (SQLException e) {
		       throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		   } finally {
		      if (con != null) {
		         try {
		           con.close();//and close the connections etc
		   		 } catch (SQLException e1) {  //if not close properly
		           e1.printStackTrace();
		         }
		   }	      
		}
		return r; //the last
	}
	
	public void delete(int id) {
		Connection con = null;	
		try {
			 //get the connection 
			 con = services.createConnection();		 
			 PreparedStatement stmt = con.prepareStatement("delete from user where userid=(?)");
		     stmt.setInt(1, id);
		     int status = stmt.executeUpdate();
			 if (status != 1)//remember to catch the exceptions
			 	  throw new DataAccessException("cannot delete any record owned by that id");
			 
		   } catch (ServiceLocatorException e) {
		       throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		   } catch (SQLException e) {
		       throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		   } finally {
		      if (con != null) {
		         try {
		           con.close();//and close the connections etc
		   		 } catch (SQLException e1) {  //if not close properly
		           e1.printStackTrace();
		         }
		   }	      
		}
		//return nothing !!
	}
	

	public UserBean getByLogin(String username, String password) throws DataAccessException {
		Connection con = null;
		UserBean user = null;
		try {
			con = services.createConnection();
			PreparedStatement stmt = con.prepareStatement("select * from user where username = (?) and password = (?)");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user = createUserBean(rs);
				stmt.close(); 
			}
		} catch (ServiceLocatorException e) {
			throw new DataAccessException("Unable to retrieve connection; " + e.getMessage(), e);
		} catch (SQLException e) {
			throw new DataAccessException("Unable to execute query; " + e.getMessage(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return user;
	}	

	//helper 
	private UserBean createUserBean(ResultSet rs) throws SQLException {
		UserBean user = new UserBean();
		user.setAddress(rs.getString("address"));
		user.setEmail(rs.getString("email"));
		user.setFname(rs.getString("fname"));
		user.setLname(rs.getString("lname"));
		user.setPassword(rs.getString("password")); //column
		user.setSecurity_level(rs.getString("security_level"));
		user.setUserid(rs.getInt("userid"));
		user.setUsername(rs.getString("username"));
		return user;
	}
}
