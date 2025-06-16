import java.sql.*;
import java.util.Scanner;

public class booking {

    private  static final String url="jdbc:mysql://localhost:3306/hotel";
    private static final String user="root";
    private static final String pass="7319425477";
    public static void main(String[] args)throws Exception {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con= DriverManager.getConnection(url,user,pass);
            Scanner sc =new Scanner(System.in);
            System.out.println("WELCOME HOTEL MANAGEMENT SYSTEM");
            System.out.println("1: RESERVE A ROOM");
            System.out.println("2: VIEW RESERVATION");
            System.out.println("3: UPDATE RESERVATION");
            System.out.println("4: DELETE RESERVATION");
            System.out.println("0: EXIT");
            System.out.println("PLEASE ENTER YOUR CHOICE: ");
            int ch=sc.nextInt();
            switch (ch){
                case 1:
                    reserveRoom(con,sc);
                    break;
                case 2:
                    viewReservation(con,sc);
                    break;
                case 3:
                    update(con,sc);
                    break;
                case 4:
                    deleteReservation(con,sc);
                    break;
                case 0:
                    System.out.println("THANK YOU FOR USING HOTEL MANAGEMENT SYSTEM.");
                    sc.close();
                    return;
                default:
                    System.out.println("INVALID CHOICE");
            }
        }
        catch (SQLException s){
            System.out.println(s.getMessage());
        }
    }

    public  static  void reserveRoom(Connection con, Scanner sc){
        try{
            sc.nextLine();
            System.out.println("ENTER YOUR NAME: ");
            String name=sc.nextLine();

            System.out.println("ENTER YOUR ROOM NUMBER: ");
            int roomNo=sc.nextInt();
            sc.nextLine();
            System.out.println("ENTER YOUR CONTACT NUMBER");
            String contact=sc.nextLine();
            String query = "INSERT INTO reservation (cust_name, room_no, contact) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, roomNo);
            stmt.setString(3, contact);

            int rs= stmt.executeUpdate();
            if(rs>0){
                System.out.println("RESERVATION SUCCESSFULLY DONE");
            }
            else{
                System.out.println("FAIL");
            }
        }
        catch (Exception E){
            System.out.println(E);
        }
    }




    public  static  void viewReservation(Connection con,Scanner sc){
        sc.nextLine();
        System.out.println("PLEASE ENTER YOUR CONTACT NUMBER");
        String num=sc.nextLine();
        try{
            String qr="select * from reservation where contact=?;";
            PreparedStatement pt=con.prepareStatement(qr);
            pt.setString(1,num);
            ResultSet rs=pt.executeQuery();
            boolean found=false;
            System.out.println("+---------+---------------+---------+------------+---------------------+");
            System.out.printf("| %-7s | %-13s | %-7s | %-10s | %-19s |\n",
                    "cust_id", "cust_name", "room_no", "contact", "booking_time");
            System.out.println("+---------+---------------+---------+------------+---------------------+");


            while (rs.next()){
                found=true;
                int id=rs.getInt("cust_id");
                String name=rs.getString("cust_name");
                int room=rs.getInt("room_no");
                String contact=rs.getString("contact");
                Timestamp date=rs.getTimestamp("booking_time");

                System.out.printf("| %-7d | %-13s | %-7d | %-10s | %-19s |\n",
                        id, name, room, contact, date);
            }
            System.out.println("+---------+---------------+---------+------------+---------------------+");

            if(!found){
                System.out.println("NO RECORD FOUND");
            }
            rs.close();
            pt.close();

        }
        catch (SQLException s){
            System.out.println(s.getMessage());
        }
    }


    public static  void deleteReservation(Connection con, Scanner sc){
        sc.nextLine();
        System.out.println("ENTER PHONE NUMBER TO DELETE THE RESERVATION");
        String num=sc.nextLine();
        try{
            String qury="delete from reservation where contact=?;";
            PreparedStatement pt=con.prepareStatement(qury);
            pt.setString(1,num);
            int rs=pt.executeUpdate();
            if(rs>0){
                System.out.println("RESERVATION SUCCESSFULLY DELETED");
            }
            else{
                System.out.println("RECORD NOT FOUND");
            }
            sc.close();
            pt.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static void update(Connection con, Scanner sc) {
        sc.nextLine(); // flush newline
        System.out.println("ENTER CONTACT NUMBER OF THE RESERVATION TO UPDATE:");
        String oldContact = sc.nextLine();

        try {
            // Check if the reservation exists
            String checkQuery = "SELECT * FROM reservation WHERE contact = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, oldContact);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("NO RESERVATION FOUND WITH THAT CONTACT.");
                return;
            }

            System.out.println("WHAT DO YOU WANT TO UPDATE?");
            System.out.println("1. CUSTOMER NAME");
            System.out.println("2. ROOM NUMBER");
            System.out.println("3. CONTACT NUMBER");
            System.out.println("4. ALL FIELDS");
            System.out.print("ENTER YOUR CHOICE: ");
            int choice = sc.nextInt();
            sc.nextLine(); // flush newline

            String updateQuery = "";
            PreparedStatement updateStmt;

            switch (choice) {
                case 1:
                    System.out.print("ENTER NEW NAME: ");
                    String newName = sc.nextLine();
                    updateQuery = "UPDATE reservation SET booking_time = NOW(), cust_name = ? WHERE contact = ?";
                    updateStmt = con.prepareStatement(updateQuery);
                    updateStmt.setString(1, newName);
                    updateStmt.setString(2, oldContact);
                    break;

                case 2:
                    System.out.print("ENTER NEW ROOM NUMBER: ");
                    int newRoom = sc.nextInt();
                    updateQuery = "UPDATE reservation SET booking_time = NOW(), room_no = ? WHERE contact = ?";
                    updateStmt = con.prepareStatement(updateQuery);
                    updateStmt.setInt(1, newRoom);
                    updateStmt.setString(2, oldContact);
                    break;

                case 3:
                    System.out.print("ENTER NEW CONTACT NUMBER: ");
                    String newContact = sc.nextLine();
                    updateQuery = "UPDATE reservation SET booking_time = NOW(), contact = ? WHERE contact = ?";
                    updateStmt = con.prepareStatement(updateQuery);
                    updateStmt.setString(1, newContact);
                    updateStmt.setString(2, oldContact);
                    break;

                case 4:
                    System.out.print("ENTER NEW NAME: ");
                    String allNewName = sc.nextLine();
                    System.out.print("ENTER NEW ROOM NUMBER: ");
                    int allNewRoom = sc.nextInt();
                    sc.nextLine();
                    System.out.print("ENTER NEW CONTACT NUMBER: ");
                    String allNewContact = sc.nextLine();
                    updateQuery = "UPDATE reservation SET booking_time = NOW(), cust_name = ?, room_no = ?, contact = ? WHERE contact = ?";
                    updateStmt = con.prepareStatement(updateQuery);
                    updateStmt.setString(1, allNewName);
                    updateStmt.setInt(2, allNewRoom);
                    updateStmt.setString(3, allNewContact);
                    updateStmt.setString(4, oldContact);
                    break;

                default:
                    System.out.println("INVALID CHOICE");
                    return;
            }

            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("RESERVATION UPDATED SUCCESSFULLY.");
            } else {
                System.out.println("UPDATE FAILED.");
            }

            rs.close();
            checkStmt.close();
            updateStmt.close();

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


}






