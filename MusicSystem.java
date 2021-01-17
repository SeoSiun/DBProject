
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicSystem {
    private static Connection connection;
    private static ResultSet rs;
    private static String sql;
    private static int currentUserNo;
    private static boolean manager=false;
    private static Scanner inFromUser = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        int command;
        boolean exit = false;

        initDBConnection();

        while(!exit) {
            System.out.println("\n\n==================== Welcome to Music System ======================");
            System.out.println("1. Log In");
            System.out.println("2. Sign Up");
            System.out.println("3. Find ID / Password");
            System.out.println("4. Exit");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    findAccount();
                    break;
                case 4:
                    System.out.println("\nGood bye...");
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command");
                    break;
            }
        }
    }

    private static void initDBConnection() throws ClassNotFoundException, SQLException {
        final String driver = "org.mariadb.jdbc.Driver";
        final String DB_IP = "localhost";
        final String DB_PORT = "3306";
        final String DB_NAME = "musicsystem";
        final String DB_URL = "jdbc:mariadb://"+ DB_IP +":" +DB_PORT +"/"+DB_NAME;
        Connection c;
        Class.forName(driver);
        c = DriverManager.getConnection(DB_URL, "root", "mya2714900!");
        if(c!=null){
            connection = c;
        }
    }

    private static void login() throws SQLException {
        String id, pw;

        while (true) {
            System.out.println("\n\n============================== Login ==============================");
            System.out.print("ID: ");
            id = inFromUser.nextLine();
            if(id.equals("-1")) return;
            System.out.print("Password: ");
            pw = inFromUser.nextLine();
            sql = "select * from manager where id='"+id+"' and password = '"+pw+"'";
            rs = getSqlResult(sql);
            if(rs.next()) {
                currentUserNo = rs.getInt(1);
                manager = true;
                System.out.println("login succeed!");
                managerMenu();
                return;
            }
            sql = "select * from user where id='"+id+"' and password = '"+pw+"'";
            rs = getSqlResult(sql);
            if(rs.next()) {
                currentUserNo = rs.getInt(1);
                manager = false;
                System.out.println("login succeed!");
                userMenu();
                return;
            }
            System.out.println("ID is not registered, or you entered the wrong Password.");
            System.out.println("If you want to go back, ENTER -1 for ID");
        }
    }

    private static void signUp() throws SQLException {
        int isManager;
        boolean isExistId;
        String id,pw;

        System.out.println("\n\n============================ Sign Up ============================");
        System.out.print("Are you manager? (manager: enter 1, user: enter 0) : ");
        isManager = inFromUser.nextInt();
        inFromUser.nextLine();
        System.out.print("Name: ");
        String name = inFromUser.nextLine();
        System.out.print("Birth Date (Format= yyyy-mm-dd): ");
        String bDate = inFromUser.nextLine();
        System.out.print("Gender (F/M): ");
        String gender = inFromUser.nextLine();
        while (true) {
            isExistId=true;
            System.out.print("ID: ");
            id = inFromUser.nextLine();
            sql = "select (select count(*) from user where user.id='"+id+"')+(select count(*) from manager where id='"+id+"')";
            rs = getSqlResult(sql);
            while (rs.next()) {
                if (rs.getInt(1) == 0) isExistId= false;
            }
            if (isExistId || id.equals("-1")) {
                System.out.println("Already exist id. Please enter another one.");
            } else break;
        }
        while(true) {
            System.out.print("Password: ");
            pw = inFromUser.nextLine();
            System.out.print("Input Password Again: ");
            String tmp = inFromUser.nextLine();
            if (pw.equals(tmp)) {
                break;
            } else {
                System.out.println("The passwords are different. Enter Password Again.");
            }
        }
        sql = "select managerNo from manager";
        rs = getSqlResult(sql);
        ArrayList<Integer> managerNo = new ArrayList<>();
        while(rs.next()) {
            managerNo.add(rs.getInt(1));
        }
        if(isManager==0) {
            if(managerNo.size() == 0){
                System.out.println(".. Sorry... No manager exist yet. Please try again later.");
                return;
            }
            sql = "insert into user(id,password,name,birth_date,gender,managerNo) values('"
                    +id+"','"+pw+"','"+name+"','"+bDate+"','"+gender+"',"+managerNo.get((int)(Math.random() * managerNo.size()))+")";
            getSqlResult(sql);
        }
        else{
            sql = "insert into manager(id,password,name,birth_date,gender) values('"+id+"','"+pw+"','"+name+"','"+bDate+"','"+gender+"')";
            getSqlResult(sql);
        }
        System.out.println("sign up succeed!");
    }


    private static void findAccount() throws SQLException {
        boolean exit = false;
        int command,isManager;
        String name, bDate, gender, ID,password, user;

        while(!exit) {
            System.out.println("\n\n======================== Find ID/Password =========================");
            System.out.println("1. Find ID");
            System.out.println("2. Change Password");
            System.out.println("3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command=inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    System.out.print("Manager or User? (manager = Enter 1, User = Enter 0) ");
                    isManager = inFromUser.nextInt();
                    inFromUser.nextLine();
                    if(isManager==1) user = "manager";
                    else user = "user";
                    System.out.print("Name: ");
                    name = inFromUser.nextLine();
                    System.out.print("Birth Date (Format = yyyy-mm-dd): ");
                    bDate = inFromUser.nextLine();
                    System.out.print("Gender (F/M): ");
                    gender = inFromUser.nextLine();
                    sql = "select id from "+user+" where name='"+name+"' and birth_date='"+bDate+"' and gender='"+gender+"'";
                    rs = getSqlResult(sql);
                    int cnt=0;
                    while(rs.next()){
                        cnt++;
                        System.out.println("ID: "+rs.getString(1));
                    }
                    if(cnt==0){
                        System.out.println("No such "+user+" exist.");
                        System.out.println("Enter to go back");
                        inFromUser.nextLine();
                        break;
                    }
                    break;
                case 2:
                    System.out.print("Manager or User? (manager - Enter 1, User - Enter 0) ");
                    isManager = inFromUser.nextInt();
                    inFromUser.nextLine();
                    if(isManager==1) user = "manager";
                    else user = "user";
                    System.out.print("ID: ");
                    ID=inFromUser.nextLine();
                    System.out.print("Name: ");
                    name = inFromUser.nextLine();
                    System.out.print("Birth Date (Format = yyyy-mm-dd): ");
                    bDate = inFromUser.nextLine();
                    System.out.print("Gender (F/M): ");
                    gender = inFromUser.nextLine();
                    sql = "select * from "+user+" where name='"+name+"' and birth_date='"+bDate+"' and gender='"+gender+"' and id = '"+ID+"'";
                    rs=getSqlResult(sql);
                    if(!rs.next()){
                        System.out.println("No such "+user+" exist.");
                        System.out.println("Enter to go back");
                        inFromUser.nextLine();
                        break;
                    }
                    while(true) {
                        System.out.print("Input new Password: ");
                        password = inFromUser.nextLine();
                        System.out.print("Input new Password Again: ");
                        String tmp = inFromUser.nextLine();
                        if (password.equals(tmp)) {
                            sql = "update "+user+" set password='" + password + "' where id = '"+ID+"'";
                            getSqlResult(sql);
                            break;
                        } else {
                            System.out.println("The passwords are different. Enter Password Again.");
                        }
                    }
                    break;
                case 3:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }

        }
    }

    private static ResultSet getSqlResult(String sql) throws SQLException {
        PreparedStatement ps;
        ps = connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /************** Manager *****************/

    private static void managerMenu() throws SQLException {
        int command, withdrawal=0;
        boolean exit =false;

        while(!exit) {
            System.out.println("\n\n=========================== Main Menu =============================");
            System.out.println("1. Manage Musics");
            System.out.println("2. Manage Users");
            System.out.println("3. Manage artists");
            System.out.println("4. Manage albums");
            System.out.println("5. Edit My Info");
            System.out.println("6. Withdrawal");
            System.out.println("7. Logout ");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");

            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    manageMusics();
                    break;
                case 2:
                    manageUsers();
                    break;
                case 3:
                    manageArtists();
                    break;
                case 4:
                    manageAlbums();
                    break;
                case 5:
                    showUserDetail(-1);
                    break;
                case 6:
                    System.out.print("Really want to withdrawal...? (Enter 'yes' for withdrawal): ");
                    String tmp = inFromUser.nextLine();
                    if(tmp.equals("yes")) withdrawal=deleteManager();
                    break;
                case 7:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
            if(withdrawal==1) break;
        }
    }

    private static void manageMusics() throws SQLException {
        boolean exit = false;
        int command;

        while(!exit) {
            System.out.println("\n\n========================== Manage Musics ==========================");
            System.out.println("1. Show music chart");
            System.out.println("2. Search music");
            System.out.println("3. Add new music");
            System.out.println("4. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");

            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    showMusicChart();
                    break;
                case 2:
                    searchMusic();
                    break;
                case 3:
                    addMusic(-1,-1);
                    break;
                case 4:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void manageUsers() throws SQLException {
        int cnt=0, userNo;
        while(true) {
            cnt=0;
            sql = "select * from user";
            rs = getSqlResult(sql);
            System.out.println("\n\n============================= User List ===========================");
            while (rs.next()) {
                cnt++;
                System.out.println(cnt + ". ID: " + rs.getString(2) + ", name: " + rs.getString(4));
            }
            if(cnt==0){
                System.out.println("No user exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select user to manage (if you want to go back, enter 0): ");
            userNo = inFromUser.nextInt();
            inFromUser.nextLine();
            if (userNo == 0) break;
            for (int i = 0; i < cnt - userNo + 1; i++) rs.previous();
            userNo = rs.getInt(1);
            showUserDetail(userNo);
        }
    }

    private static void manageArtists() throws SQLException {
        int command;
        boolean exit = false;

        while(!exit) {
            System.out.println("\n\n========================== Manage Artists =========================");
            System.out.println("1. Show All Artist");
            System.out.println("2. Add new Artist");
            System.out.println("3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    showAllArtist();
                    break;
                case 2:
                    addArtist();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void manageAlbums() throws SQLException {
        int command;
        boolean exit = false;

        while(!exit) {
            System.out.println("\n\n========================== Manage Albums ==========================");
            System.out.println("1. Show All Albums");
            System.out.println("2. Add new Album");
            System.out.println("3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    showAllAlbums();
                    break;
                case 2:
                    addAlbum(-1);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    /******************** User *********************/

    private static void userMenu() throws SQLException {
        int command, withdrawal=0;
        boolean exit =false;

        while(!exit) {
            System.out.println("\n\n============================ Main Menu ============================");
            System.out.println("1. Show music chart");
            System.out.println("2. Search music");
            System.out.println("3. Show All Public PlayLists");
            System.out.println("4. Show All Albums");
            System.out.println("5. Show All Artists");
            System.out.println("6. My Page");
            System.out.println("7. Logout ");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    showMusicChart();
                    break;
                case 2:
                    searchMusic();
                    break;
                case 3:
                    showAllPublicPlayList();
                    break;
                case 4:
                    showAllAlbums();
                    break;
                case 5:
                    showAllArtist();
                    break;
                case 6:
                    withdrawal = myPage();
                    break;
                case 7:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
            if(withdrawal==1) break;
        }
    }

    private static int myPage() throws SQLException {
        int command;
        boolean exit =false;

        while(!exit) {
            System.out.println("\n\n============================= My Page =============================");
            System.out.println("1. My Like list");
            System.out.println("2. My PlayLists");
            System.out.println("3. My comments");
            System.out.println("4. Edit My Info");
            System.out.println("5. Withdrawal");
            System.out.println("6. Go back ");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    showMusicILike();
                    break;
                case 2:
                    showMyPlayList();
                    break;
                case 3:
                    showAllCommentForUser();
                    break;
                case 4:
                    showUserDetail(-1);
                    break;
                case 5:
                    System.out.print("Really want to withdrawal...? (Enter 'yes' for withdrawal): ");
                    String tmp = inFromUser.nextLine();
                    if(tmp.equals("yes")){
                        deleteUser(currentUserNo);
                        return 1;
                    }
                case 6:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
       return 0;
    }

    private static void showAllPublicPlayList() throws SQLException {
        int cnt;
        while(true) {
            System.out.println("\n\n====================== Show All Public PlayList ======================");
            sql = "select title, id, user.userNo from playlist,user where user.userNo=playlist.userNo and isOpen=" + 1;
            rs = getSqlResult(sql);
            cnt = 0;
            while (rs.next()) {
                cnt++;
                System.out.println(cnt+". "+rs.getString(1)+" - "+rs.getString(2));
            }
            if(cnt == 0){
                System.out.println("No Public PlayList exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select playList to see detail (if you want to go back, enter 0): ");
            int playlistNo = inFromUser.nextInt();
            if (playlistNo == 0) break;
            inFromUser.nextLine();
            for (int i = 0; i < cnt - playlistNo + 1; i++) rs.previous();
            showPlayListDetail(rs.getString(1),rs.getInt(3));
        }
    }

    private static void showMusicILike() throws SQLException {
        int cnt,command;
        ResultSet rs2;

        while(true) {
            System.out.println("\n\n======================= Show My Like List =========================");
            sql = "select title, musicNo from music where musicNo in" +
                    "(select musicNo from likes where userNo= " + currentUserNo + ")";
            rs = getSqlResult(sql);
            cnt = 0;
            while (rs.next()) {
                cnt++;
                System.out.print(cnt + ". " + rs.getString(1) + " - ");
                sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                int c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if (cnt == 0){
                System.out.println("No music in like list.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
            System.out.println("\n1. Unlike");
            System.out.println("2. Play music");
            System.out.println("3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command=inFromUser.nextInt();
            inFromUser.nextLine();
            if(command==1 || command==2) {
                System.out.print("Select music: ");
                int musicNo = inFromUser.nextInt();
                inFromUser.nextLine();
                for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                musicNo = rs.getInt(2);
                if (command == 1) {
                    sql = "delete from likes where userNo=" + currentUserNo + " and musicNo=" + musicNo;
                    getSqlResult(sql);
                } else {
                    playMusic(musicNo);
                }
            }else if(command==3) break;
            else System.out.println("Wrong command.");
        }
    }

    private static void showMyPlayList() throws SQLException {
        int cnt = 0, cmd, playlistNo;
        boolean exit = false;
        String title;

        while (!exit) {
            cnt = 0;
            System.out.println("\n\n========================= Show My PlayList =========================");
            sql = "select * from playlist where userNo=" + currentUserNo;
            rs = getSqlResult(sql);
            while (rs.next()) {
                cnt++;
                System.out.println(cnt + ". " + rs.getString(1));
            }
            if (cnt == 0) {
                System.out.println("No playlist exist.");
            }
            System.out.println("\n1. Add PlayList");
            System.out.println("2. Show PlayList detail");
            System.out.println("3. Delete PlayList");
            System.out.println("4. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            cmd = inFromUser.nextInt();
            inFromUser.nextLine();
            switch (cmd) {
                case 1:
                    addPlaylist();
                    break;
                case 2:
                    System.out.print("Select playlist: ");
                    playlistNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - playlistNo + 1; i++) rs.previous();
                    title = rs.getString(1);
                    showPlayListDetail(title, currentUserNo);
                    break;
                case 3:
                    System.out.print("Select playlist: ");
                    playlistNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - playlistNo + 1; i++) rs.previous();
                    title = rs.getString(1);
                    deletePlaylist(title);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
            }
        }
    }

    private static void showAllCommentForUser() throws SQLException {
        int cnt;
        while(true) {
            System.out.println("\n\n======================== Show My Comments =========================");
            sql = "select title, content, writeDate, music.musicNo from music, comment where music.musicNo=comment.musicNo and userNo= " + currentUserNo +" order by writeDate";
            rs = getSqlResult(sql);
            cnt = 0;
            while (rs.next()) {
                cnt++;
                System.out.println(cnt + ". " + rs.getString(1));
                System.out.println("  "+rs.getString(2)+" - "+rs.getDate(3));
            }
            if (cnt == 0){
                System.out.println("No comment exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                break;
            }

            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select comment to delete (If you want to go back, enter 0): ");

            int commentNo = inFromUser.nextInt();
            if (commentNo == 0) break;
            inFromUser.nextLine();
            for (int i = 0; i < cnt - commentNo + 1; i++) rs.previous();
            int musicNo= rs.getInt(4);
            String comment = rs.getString(2);
            sql = "delete from comment where musicNo="+musicNo+" and userNo="+currentUserNo+" and content='"+comment+"'";
            getSqlResult(sql);
        }
    }

    private static void playMusic(int musicNo) throws SQLException {
        sql = "select * from music where musicNo="+musicNo;
        rs = getSqlResult(sql);
        rs.next();
        System.out.println("\n[ "+rs.getString("title")+" ]");
        System.out.println("0:01 -o-------------------------- "+rs.getTime("playtime"));
        System.out.println("       <<         ||         >>         ");
        sql = "select count(*) from play where musicNo="+musicNo+" and userNo="+currentUserNo;
        rs=getSqlResult(sql);
        rs.next();
        if(rs.getInt(1)==0){
            sql = "insert into play values("+musicNo+","+currentUserNo+",0)";
            getSqlResult(sql);
        }
        sql = "update play set cnt_play=cnt_play+1 where musicNo=" + musicNo + " and userNo=" + currentUserNo;
        getSqlResult(sql);
        System.out.println("Enter to go back");
        inFromUser.nextLine();
    }

    /************** Show ****************/

    private static void showMusicChart() throws SQLException {
        int musicNo,cnt=0;
        ResultSet r, rs2;

        System.out.println("\n\n======================== Show Music Chart =========================");

        sql = "select title, music.musicNo, sum(cnt_play) from music join play on music.musicNo = play.musicNo group by music.musicNo order by sum(cnt_play) desc";
        rs = getSqlResult(sql);
        while (rs.next()){
            cnt++;
            System.out.print(cnt+". "+rs.getString(1)+" - ");
            sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
            rs2=getSqlResult(sql);
            int c=0;
            while (rs2.next()) {
                if(c!=0) System.out.print(", ");
                System.out.print(rs2.getString(1));
                c++;
            }
            System.out.println();
        }
        int check = cnt;
        sql = "select title, music.musicNo from music where music.musicNo not in (select music.musicNo from music join play on music.musicNo = play.musicNo)";
        r = getSqlResult(sql);
        while(r.next()){
            check++;
            System.out.print(check+". "+r.getString(1)+" - ");
            sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+r.getInt(2);
            rs2=getSqlResult(sql);
            int c=0;
            while (rs2.next()) {
                if(c!=0) System.out.print(", ");
                System.out.print(rs2.getString(1));
                c++;
            }
            System.out.println();
        }
        if(cnt==0 && check==0){
            System.out.println("No music exist.");
            System.out.println("Enter to go back");
            inFromUser.nextLine();
            return;
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.print("Select music to see detail: ");
        musicNo = inFromUser.nextInt();
        inFromUser.nextLine();
        if(musicNo<=cnt) {
            for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
            musicNo = rs.getInt(2);
        }
        else{
            for (int i = 0; i < check - musicNo + 1; i++) r.previous();
            musicNo = r.getInt(2);
        }
        if(manager)managerMusic(musicNo);
        else userMusic(musicNo);
    }

    private static void searchMusic() throws SQLException {
        int musicNo;
        System.out.println("\n\n========================= Search Music ============================");
        System.out.print("Input keyword: ");
        String keyword = inFromUser.nextLine();
        sql = "select title,musicNo from music where title like '%" + keyword + "%'";
        rs = getSqlResult(sql);
        int cnt = 0;
        while (rs.next()) {
            cnt++;
            System.out.println(cnt + ". " + rs.getString(1));
        }
        if (cnt == 0) {
            System.out.println("No such music exist.");
            System.out.println("Enter to Go back");
            inFromUser.nextLine();
            return;
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.print("Select music number to see detail: ");
        musicNo = inFromUser.nextInt();
        inFromUser.nextLine();
        for(int i=0; i<cnt-musicNo+1; i++) rs.previous();
        musicNo = rs.getInt(2);
        if(manager) managerMusic(musicNo);
        else userMusic(musicNo);
    }

    private static void showAllAlbums() throws SQLException {
        int cnt=0,albumNo;
        ResultSet rs2;

        while(true) {
            cnt=0;
            System.out.println("\n\n======================= Show All Album List =======================");

            sql = "select title,albumNo from album";
            rs = getSqlResult(sql);
            while (rs.next()) {
                cnt++;
                System.out.print(cnt + ". " + rs.getString(1) + " - ");
                sql = "select name from artist, make_album where artist.artistNo=make_album.artistNo and albumNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                int c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if(cnt==0){
                System.out.println("No album exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                break;
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select album to see detail (If you want to go back, enter 0): ");
            albumNo = inFromUser.nextInt();
            inFromUser.nextLine();
            if(albumNo==0) break;
            for (int i = 0; i < cnt - albumNo + 1; i++) rs.previous();
            albumNo = rs.getInt(2);
            if(manager) managerAlbum(albumNo);
            else userAlbum(albumNo);
        }
    }

    private static void showAllArtist() throws SQLException {
        int cnt,artistNo;
        ResultSet r;
        boolean exit;
        while(true) {
            cnt=0;
            sql = "select * from artist";
            r = getSqlResult(sql);
            System.out.println("\n\n=========================== Artist List ===========================");
            while (r.next()) {
                cnt++;
                System.out.println(cnt + ". " + r.getString(3));
            }
            if(cnt==0){
                System.out.println("No artist exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select artist (if you want to go back, enter 0): ");
            artistNo = inFromUser.nextInt();
            inFromUser.nextLine();
            if (artistNo == 0) return;
            for (int i = 0; i < cnt - artistNo + 1; i++) r.previous();
            artistNo = r.getInt(1);
            artistDetail(artistNo);
        }
    }

    private static void artistDetail(int artistNo) throws SQLException {
        int command;
        boolean exit=false;

        while (!exit) {
            sql = "select * from artist where artistNo="+artistNo;
            rs= getSqlResult(sql);
            rs.next();
            System.out.println("\n\n========================== Artist Detail ==========================");
            System.out.println("Name: " + rs.getString(3) + ", Debut Date: " + rs.getDate(2));
            System.out.println("\n1. Show members");
            System.out.println("2. Show musics");
            System.out.println("3. Show albums");
            if(manager) {
                System.out.println("4. Delete artist");
                System.out.println("5. Go back");
            }
            else System.out.println("4. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command) {
                case 1:
                    showMembers(artistNo);
                    break;
                case 2:
                    showArtistsMusics(artistNo);
                    break;
                case 3:
                    showArtistsAlbums(artistNo);
                    break;
                case 4:
                    if(!manager){
                        exit=true;
                        break;
                    }
                    System.out.println("WARNING : DELETE Artist -> Artist's Musics & Albums also DELETE.\nIf you still want to DELETE Artist, ENTER 'yes'");
                    String delete = inFromUser.nextLine();
                    if (delete.equals("yes")) {
                        deleteArtist(artistNo);
                        System.out.println("Delete succeed!");
                        System.out.println("Enter to go back");
                        inFromUser.nextLine();
                        exit = true;
                    }
                    break;
                case 5:
                    if(manager) {
                        exit = true;
                        break;
                    }
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void showMembers(int artistNo) throws SQLException {
        int cnt;
        String name;

        while(true) {
            cnt =0;
            System.out.println("\n\n====================== Artist's Member List =======================");
            sql = "select mName from artist_member where artistNo="+artistNo;
            rs = getSqlResult(sql);
            while (rs.next()) {
                cnt++;
                System.out.println(cnt+". "+rs.getString(1));
            }
            if(!manager){
                System.out.println("\nEnter to go back");
                inFromUser.nextLine();
                break;
            }
            else {
                if (cnt == 0) System.out.println("No member exist.");
                System.out.println("\n1. Add Member");
                System.out.println("2. Delete Member");
                System.out.println("3. Go back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input command: ");
                int cmd = inFromUser.nextInt();
                inFromUser.nextLine();
                if (cmd == 1) {
                    System.out.print("Input member name: ");
                    name = inFromUser.nextLine();
                    sql = "insert into artist_member values(" + artistNo + ", '" + name + "')";
                    getSqlResult(sql);
                } else if (cmd == 2) {
                    System.out.print("Input member number to delete: ");
                    int memberNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - memberNo + 1; i++) rs.previous();
                    name = rs.getString(1);
                    sql = "delete from artist_member where artistNo=" + artistNo + " and mName='" + name + "'";
                    getSqlResult(sql);
                } else if (cmd == 3) break;
                else {
                    System.out.println("Wrong command.");
                }
            }
        }
    }

    private static void showArtistsMusics(int artistNo) throws SQLException {
        int cnt;
        ResultSet rs2;
        while(true) {
            cnt=0;
            System.out.println("\n\n========================= Artist's Musics ==========================");
            sql = "select title,music.musicNo from artist,make_music,music where make_music.artistNo = artist.artistNo and artist.artistNo=" + artistNo +" and make_music.musicNo=music.musicNo";
            rs = getSqlResult(sql);
            while (rs.next()) {
                cnt++;
                System.out.print(cnt+". "+rs.getString(1)+" - ");
                sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                int c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if(manager) {
                if (cnt == 0) System.out.println("No music exist.");
                System.out.println("\n1. Add Music");
                System.out.println("2. Delete Music");
                System.out.println("3. Go back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input command: ");
                int cmd = inFromUser.nextInt();
                inFromUser.nextLine();
                if (cmd == 1) {
                    addMusic(-1, artistNo);
                } else if (cmd == 2) {
                    System.out.print("Input music number to delete: ");
                    int musicNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                    musicNo = rs.getInt(2);
                    deleteMusic(musicNo);
                } else if (cmd == 3) break;
                else {
                    System.out.println("Wrong command.");
                }
            }
            else{
                if (cnt == 0){
                    System.out.println("No music exist.");
                    System.out.println("Enter to go back");
                    inFromUser.nextLine();
                    break;
                }
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Select music number to see detail (Enter 0 to go back): ");
                int musicNo = inFromUser.nextInt();
                inFromUser.nextLine();
                if(musicNo==0) break;
                for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                musicNo = rs.getInt(2);
                userMusic(musicNo);
            }
        }
    }


    private static void showArtistsAlbums(int artistNo) throws SQLException {
        int cnt =0;
        ResultSet rs2;
        while(true) {
            cnt=0;
            System.out.println("\n\n======================== Artist's Album List ========================");
            sql = "select title,album.albumNo from artist,make_album,album where make_album.artistNo=artist.artistNo and artist.artistNo=" + artistNo+" and make_album.albumNo=album.albumNo";
            rs = getSqlResult(sql);
            while (rs.next()) {
                cnt++;
                System.out.print(cnt+". "+rs.getString(1)+" - ");
                sql = "select name from artist, make_album where artist.artistNo=make_album.artistNo and albumNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                int c = 0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if(manager) {
                if (cnt == 0) System.out.println("No album exist.");
                System.out.println("\n1. Add Album");
                System.out.println("2. Delete Album");
                System.out.println("3. Go back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input command: ");
                int cmd = inFromUser.nextInt();
                inFromUser.nextLine();
                if (cmd == 1) {
                    addAlbum(artistNo);
                } else if (cmd == 2) {
                    System.out.print("\nInput album number to delete: ");
                    int albumNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - albumNo + 1; i++) rs.previous();
                    albumNo = rs.getInt(2);
                    deleteAlbum(albumNo);
                } else if (cmd == 3) break;
                else {
                    System.out.println("Wrong command.");
                }
            }
            else{
                if (cnt == 0){
                    System.out.println("No album exist.");
                    System.out.println("Enter to go back");
                    inFromUser.nextLine();
                    break;
                }
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Select album to see detail (If you want to go back, Enter 0): ");
                int albumNo = inFromUser.nextInt();
                inFromUser.nextLine();
                if(albumNo==0) break;
                for (int i = 0; i < cnt - albumNo + 1; i++) rs.previous();
                albumNo = rs.getInt(2);
                userAlbum(albumNo);
            }
        }
    }

    private static void managerAlbum(int albumNo) throws SQLException {
        int cnt=0,command;
        boolean exit=false;
        ResultSet rs2;

        while(!exit) {
            cnt=0;
            System.out.println("\n\n========================= Album Detail ============================");
            sql = "select title,releaseDate from album where album.albumNo="+albumNo;
            rs = getSqlResult(sql);
            rs.next();
            System.out.println("Title: "+ rs.getString(1));
            sql = "select name from artist, make_album where artist.artistNo=make_album.artistNo and albumNo ="+albumNo;
            rs2=getSqlResult(sql);
            System.out.print("Artist: ");
            int c=0;
            while (rs2.next()) {
                if(c!=0) System.out.print(", ");
                System.out.print(rs2.getString(1));
                c++;
            }
            System.out.println();
            System.out.println("Release Date: " + rs.getDate(2));
            sql = "select title, musicNo from music where albumNo="+albumNo;
            rs=getSqlResult(sql);
            while(rs.next()){
                cnt++;
                System.out.print("\t"+cnt+". "+rs.getString(1)+" - ");
                sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if(cnt ==0 ){
                System.out.println("No music exist.");
            }
            System.out.println("\n1. Edit Album");
            System.out.println("2. Delete Album");
            System.out.println("3. Delete Music from album");
            System.out.println("4. Add Music");
            System.out.println("5. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    editAlbum(albumNo);
                    break;
                case 2:
                    deleteAlbum(albumNo);
                    exit=true;
                    break;
                case 3:
                    System.out.print("Input music number to delete (Enter 0 to cancel): ");
                    int musicNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    if(musicNo==0) break;
                    for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                    musicNo = rs.getInt(2);
                    sql = "update music set albumNo=null where musicNo="+musicNo;
                    getSqlResult(sql);
                    break;
                case 4:
                    addMusic(albumNo,-1);
                    break;
                case 5:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void userAlbum(int albumNo) throws SQLException {
        int cnt=0,musicNo;
        ResultSet rs2;

        while(true) {
            cnt=0;
            System.out.println("\n\n========================= Album Detail ============================");
            sql = "select title,releaseDate from album where album.albumNo="+albumNo;
            rs = getSqlResult(sql);
            rs.next();
            System.out.println("Title: "+ rs.getString(1));
            sql = "select name from artist, make_album where artist.artistNo=make_album.artistNo and albumNo ="+albumNo;
            rs2=getSqlResult(sql);
            System.out.print("Artist: ");
            int c=0;
            while (rs2.next()) {
                if(c!=0) System.out.print(", ");
                System.out.print(rs2.getString(1));
                c++;
            }
            System.out.println();
            System.out.println("Release Date: " + rs.getDate(2));
            sql = "select title, musicNo from music where albumNo="+albumNo;
            rs=getSqlResult(sql);
            while(rs.next()){
                cnt++;
                System.out.print("\t"+cnt+". "+rs.getString(1)+" - ");
                sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(", ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if(cnt==0){
                System.out.println("\nNo music exist.");
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Select music to show detail (if you want to go back, Enter 0): ");
            musicNo = inFromUser.nextInt();
            inFromUser.nextLine();
            if(musicNo==0) return;
            for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
            musicNo = rs.getInt(2);
            userMusic(musicNo);
        }
    }

    private static void managerMusic(int musicNo) throws SQLException {
        int command;
        boolean exit = false;

        while(!exit) {
            showMusicDetail(musicNo);
            System.out.println("1. Delete Music");
            System.out.println("2. Manage Comments");
            System.out.println("3. Edit Music");
            System.out.println("4. Go Back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command) {
                case 1:
                    deleteMusic(musicNo);
                    exit=true;
                    break;
                case 2:
                    showAllCommentForMusic(musicNo);
                    break;
                case 3:
                    editMusic(musicNo);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void userMusic(int musicNo) throws SQLException {
        int command,like;
        boolean exit = false;
        while (!exit) {
            showMusicDetail(musicNo);
            sql = "select count(*) from likes where userNo=" + currentUserNo + " and musicNo=" + musicNo;
            rs = getSqlResult(sql);
            rs.next();
            like = rs.getInt(1);
            System.out.println("1. Play music");
            System.out.println("2. Show Comments");
            if(like==1) System.out.println("3. Unlike");
            else if (like==0) System.out.println("3. Like");
            System.out.println("4. Add to Playlist");
            System.out.println("5. Go Back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command) {
                case 1:
                    playMusic(musicNo);
                    break;
                case 2:
                    showAllCommentForMusic(musicNo);
                    break;
                case 3:
                    if(like==1) sql = "delete from likes where userNo=" + currentUserNo + " and musicNo=" + musicNo;
                    else if(like==0) sql = "insert into likes values(" + musicNo + " ," + currentUserNo+")";
                    getSqlResult(sql);
                    break;
                case 4:
                    addToPlayList(musicNo);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void showMusicDetail(int musicNo) throws SQLException {
        ResultSet rs1, rs2, rs3;
        sql = "select * from music where musicNo = "+musicNo;
        rs1=getSqlResult(sql);

        System.out.println("\n\n=========================== Music Detail ==========================");
        rs1.next();
        System.out.println("Title: "+rs1.getString(3));
        sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+musicNo;
        rs2=getSqlResult(sql);
        System.out.print("Artist: ");
        int c=0;
        while (rs2.next()) {
            if(c!=0) System.out.print(" / ");
            System.out.print(rs2.getString(1));
            c++;
        }
        System.out.println();
        System.out.println("Genre: "+rs1.getString(2));
        sql = "select album.title from album,music where music.albumNo=album.albumNo and musicNo= "+musicNo;
        rs3=getSqlResult(sql);
        if(rs3.next()) System.out.println("Album: "+rs3.getString(1));
        System.out.print("Time: "+rs1.getTime(5));
        System.out.println("\t\t\tRelease Date: "+rs1.getDate(6));
        sql = "select count(*) from likes where musicNo="+musicNo+" group by musicNo";
        rs3=getSqlResult(sql);
        if(!rs3.next()) System.out.print("Like: 0");
        else System.out.print("Like: "+rs3.getInt(1));
        sql = "select sum(cnt_play) from play where musicNo="+musicNo+" group by musicNo";
        rs3=getSqlResult(sql);
        if(!rs3.next()) System.out.print("\t\tPlay Time: 0");
        else System.out.print("\t\tPlay Time: " +rs3.getInt(1));
        sql = "select count(*) from comment where musicNo="+musicNo+" group by musicNo";
        rs3=getSqlResult(sql);
        if(!rs3.next()) System.out.println("\t\tComments: 0");
        else System.out.println("\t\tComments: "+rs3.getInt(1));
        System.out.println("Lyrics:\n"+rs1.getString(4));
    }

    private static void showAllCommentForMusic(int musicNo) throws SQLException {
        int cnt=0, command;
        sql = "select content,name,writeDate,user.userNo from comment,user where comment.userNo = user.userNo and musicNo=" + musicNo+" order by writeDate";
        rs = getSqlResult(sql);
        System.out.println("\n\n========================== Show Comments ==========================");
        while (rs.next()) {
            cnt++;
            System.out.print(cnt+". "+rs.getString(1)+" - ");
            System.out.print(rs.getString(2)+" - ");
            System.out.println(rs.getDate(3));
        }
        if(cnt == 0){
            System.out.println("No comment exist.");
            if(manager){
                System.out.println("Enter to go back");
                inFromUser.nextLine();
                return;
            }
        }

        if(!manager){
            System.out.println("\n1. Write Comment");
            System.out.println("2. Go back");
        }
        System.out.println("-------------------------------------------------------------------");
        if(manager) {
            System.out.print("Select comment to delete (if you want to go back, enter 0): ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();
            if (command == 0) return;
            for (int i = 0; i < cnt - command + 1; i++) rs.previous();
            int userNo = rs.getInt(4);
            String content = rs.getString(1);
            sql = "delete from comment where userNo=" + userNo + " and musicNo=" + musicNo+" and content='"+content+"'";
            getSqlResult(sql);
        }
        else{
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();
            if(command==1){
                System.out.print("Comment: ");
                String comment = inFromUser.nextLine();
                java.util.Date date = new java.util.Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                sql = "insert into comment values("+musicNo+","+currentUserNo+",'"+format.format(date)+"','"+comment+"')";
                getSqlResult(sql);
            }
            else if(command == 2) return;
            else{
                System.out.println("Wrong command.");
            }
        }
    }

    private static void showUserDetail(int userNo) throws SQLException {
        int command;
        int isMy=0;
        boolean exit =false;
        if(userNo==-1){
            isMy=1;
            userNo=currentUserNo;
        }
        while (!exit) {
            if(isMy==1 && manager) sql = "select * from manager where managerNo="+userNo;
            else sql = "select * from user where userNo="+userNo;
            rs = getSqlResult(sql);
            rs.next();
            if(isMy==1 && manager) System.out.println("\n\n========================== Manager Detail ==========================");
            else System.out.println("\n\n=========================== User Detail ===========================");
            System.out.println("ID: " + rs.getString(2) + "\nName: " + rs.getString(4) +
                    "\nBirth Date: " + rs.getDate(5) + "\nGender: " + rs.getString(6));

            if(isMy==0 && manager) {
                System.out.println("\n1. Edit user");
                System.out.println("2. Delete user");
                System.out.println("3. Go Back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input command: ");
                command = inFromUser.nextInt();
                inFromUser.nextLine();

                switch (command) {
                    case 1:
                        editUser(userNo,isMy);
                        break;
                    case 2:
                        deleteUser(userNo);
                        exit=true;
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Wrong command.");
                        break;
                }
            }
            else{
                editUser(userNo,isMy);
                exit=true;
            }
        }
    }

    private static void showPlayListDetail(String title, int userNo) throws SQLException {
        int command,cnt;
        ResultSet rs2;

        while(true) {
            System.out.println("\n\n====================== PlayList : " + title + " =======================");
            sql = "select title,isOpen,id from playlist,user where title='" + title + "' and user.userNo=playlist.userNo and playlist.userNo=" + userNo;
            rs = getSqlResult(sql);
            rs.next();
            int isOpen = rs.getInt(2);
            if (isOpen == 1) System.out.println("Public PlayList.");
            else if (isOpen == 0) System.out.println("Private PlayList.");
            System.out.println("Owner ID: " + rs.getString(3));
            sql = "select title, musicNo from music where musicNo in " +
                    "(select musicNo from add_to_playlist where title='" + title + "' and userNo=" + userNo + ")";
            rs = getSqlResult(sql);
            cnt = 0;
            while (rs.next()) {
                cnt++;
                System.out.print(cnt + ". " + rs.getString(1) + " - ");
                sql = "select name from artist, make_music where artist.artistNo=make_music.artistNo and musicNo ="+rs.getInt(2);
                rs2=getSqlResult(sql);
                int c=0;
                while (rs2.next()) {
                    if(c!=0) System.out.print(" / ");
                    System.out.print(rs2.getString(1));
                    c++;
                }
                System.out.println();
            }
            if (userNo == currentUserNo) {
                if (cnt == 0) System.out.println("No music in playlist.");
                if (isOpen == 1) System.out.println("\n1. Change to Private playlist");
                else if (isOpen == 0) System.out.println("\n1. Change to Public playlist");
                System.out.println("2. Edit title");
                System.out.println("3. Delete music in playlist");
                System.out.println("4. Play music");
                System.out.println("5. Like");
                System.out.println("6. Go back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input Command: ");
                command = inFromUser.nextInt();
                inFromUser.nextLine();
                if(command==6) break;
                else if (command == 1) {
                    if (isOpen == 1)
                        sql = "update playlist set isOpen=0 where title='" + title + "' and userNo=" + userNo;
                    else if (isOpen == 0)
                        sql = "update playlist set isOpen=1 where title='" + title + "' and userNo=" + userNo;
                    getSqlResult(sql);
                } else if(command==2){
                    System.out.print("Input new Title: ");
                    String newTitle = inFromUser.nextLine();
                    sql = "update playlist set title='"+newTitle+"' where title ='"+title+"' and userNo="+userNo;
                    getSqlResult(sql);
                    title = newTitle;
                } else if (command == 3 || command == 4 || command==5) {
                    if (cnt == 0) {
                        System.out.println("No music exist in this playlist.");
                        System.out.println("Enter to go back.");
                        inFromUser.nextLine();
                    } else {
                        System.out.print("Select music: ");
                        int musicNo = inFromUser.nextInt();
                        inFromUser.nextLine();
                        for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                        musicNo = rs.getInt(2);
                        if(command==3) {
                            sql = "delete from add_to_playlist where musicNo=" + musicNo + " and userNo=" + userNo + " and title='" + title + "'";
                            getSqlResult(sql);
                        }
                        else if(command==4){
                            playMusic(musicNo);
                        }
                        else{
                            sql = "select count(*) from likes where musicNo=" + musicNo + " and userNo=" + currentUserNo;
                            rs = getSqlResult(sql);
                            rs.next();
                            if (rs.getInt(1) == 0) {
                                sql = "insert into likes values(" + musicNo + " ," + currentUserNo + ")";
                                getSqlResult(sql);
                            } else {
                                System.out.println("You already Like this music.");
                                System.out.println("To Unlike it. Enter 'yes'");
                                String tmp=inFromUser.nextLine();
                                if(tmp.equals("yes")) {
                                    sql = "delete from likes where musicNo=" + musicNo + " and userNo=" + currentUserNo;
                                    getSqlResult(sql);
                                }
                            }
                        }
                    }
                } else System.out.println("Wrong command.");
            } else {
                if (cnt == 0) {
                    System.out.println("No music exist in this playlist.");
                    System.out.println("Enter to go back");
                    inFromUser.nextLine();
                    return;
                }
                System.out.println("\n1. Add music to my playlist");
                System.out.println("2. Play music");
                System.out.println("3. Like");
                System.out.println("4. Go back");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Input Command: ");
                command = inFromUser.nextInt();
                inFromUser.nextLine();
                if (command == 2 || command == 1 || command == 3) {
                    System.out.print("Select music: ");
                    int musicNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - musicNo + 1; i++) rs.previous();
                    musicNo = rs.getInt(2);
                    if (command == 1) addToPlayList(musicNo);
                    else if (command == 2) {
                        playMusic(musicNo);
                    } else {
                        sql = "select count(*) from likes where musicNo=" + musicNo + " and userNo=" + currentUserNo;
                        rs = getSqlResult(sql);
                        rs.next();
                        if (rs.getInt(1) == 0) {
                            sql = "insert into likes values(" + musicNo + " ," + currentUserNo + ")";
                            getSqlResult(sql);
                        } else {
                            System.out.println("You already Like this music.");
                            System.out.println("To Unlike it. Enter 'yes'");
                            String tmp=inFromUser.nextLine();
                            if(tmp.equals("yes")) {
                                sql = "delete from likes where musicNo=" + musicNo + " and userNo=" + currentUserNo;
                                getSqlResult(sql);
                            }
                        }
                    }
                } else if (command == 4) break;
                else System.out.println("Wrong command.");
            }
        }
    }

    /************************ Add ***************************/

    private static void addMusic(int albumNo, int artistNo) throws SQLException {
        String title,artist,album="t",lyrics,playtime,genre,releaseDate;
        int cnt;
        ArrayList<Integer> artistNumbers = new ArrayList<>();
        ResultSet rs2;

        System.out.println("\n\n=========================== Add Music ===========================");
        System.out.print("Title: ");
        title = inFromUser.nextLine();
        if(artistNo==-1) {
            while (true) {
                cnt = 0;
                System.out.print("Artist (If you want to stop, Enter q): ");
                artist = inFromUser.nextLine();
                if(artist.equals("q")) break;
                sql = "select * from artist where name like '%" + artist + "%'";
                rs = getSqlResult(sql);
                while (rs.next()) cnt++;
                if (cnt == 0) System.out.println("no such artist exist. Try again.");
                else {
                    cnt = 0;
                    while (rs.previous()) ;
                    while (rs.next()) {
                        cnt++;
                        System.out.println(cnt + ". " + rs.getString(3) + "   " + rs.getDate(2));
                    }
                    System.out.println("-------------------------------------------------------------------");
                    System.out.print("Select artist: ");
                    int tmp = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - tmp + 1; i++) rs.previous();
                    artistNumbers.add(rs.getInt(1));
                }
            }
        }
        System.out.print("Genre: ");
        genre = inFromUser.nextLine();
        System.out.print("Play Time (format = mm:ss): ");
        playtime = inFromUser.nextLine();
        System.out.print("Release Date (format = yyyy-mm-dd): ");
        releaseDate = inFromUser.nextLine();
        if(albumNo==-1) {
            while (true) {
                cnt = 0;
                System.out.print("Album (if no album, input n) : ");
                album = inFromUser.nextLine();
                if (album.equals("n")) break;
                sql = "select album.albumNo,releaseDate,title,name from album,make_album,artist where artist.artistNo=make_album.artistNo and make_album.albumNo=album.albumNo and title like '%" + album+"%' group by albumNo";
                rs = getSqlResult(sql);
                while (rs.next()) cnt++;
                if (cnt == 0) System.out.println("no such album exist. Try again.");
                else {
                    cnt = 0;
                    while (rs.previous());
                    while (rs.next()) {
                        cnt++;
                        System.out.print(cnt + ". " + rs.getString(3) + " - ");
                        sql = "select name from artist, make_album where artist.artistNo=make_album.artistNo and albumNo ="+rs.getInt(1);
                        rs2=getSqlResult(sql);
                        int c=0;
                        while (rs2.next()) {
                            if(c!=0) System.out.print(" / ");
                            System.out.print(rs2.getString(1));
                            c++;
                        }
                        System.out.println(" - "+ rs.getDate(2));
                    }
                    System.out.println("-------------------------------------------------------------------");
                    System.out.print("Select album: ");
                    albumNo = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - albumNo + 1; i++) rs.previous();
                    albumNo = rs.getInt(1);
                    break;
                }
            }
        }
        String tmp;
        lyrics="";
        System.out.println("Lyrics (if want to stop input, enter 'q') ");
        while(true) {
            tmp = inFromUser.nextLine();
            if(tmp.equals("q")) break;
            lyrics+=(tmp+"\n");
        }
        if(album.equals("n")) sql = "insert into music(genre,title,lyrics,playtime,releaseDate,managerNo) values('"
                +genre+"','"+title+"','"+lyrics+"','00:"+playtime+"','"+releaseDate+"',"+currentUserNo+")";
        else sql = "insert into music(genre,title,lyrics,playtime,releaseDate,albumNo,managerNo) values('"
                +genre+"','"+title+"','"+lyrics+"','00:"+playtime+"','"+releaseDate+"',"+albumNo+","+currentUserNo+")";
        getSqlResult(sql);

        sql = "select max(musicNo) from music";
        rs = getSqlResult(sql);
        rs.next();
        int musicNo = rs.getInt(1);
        if(artistNo!=-1) artistNumbers.add(artistNo);
        for(int i=0; i<artistNumbers.size(); i++){
            sql = "insert into make_music values(" + artistNumbers.get(i) + "," + musicNo + ")";
            getSqlResult(sql);
        }
    }

    private static void addAlbum(int artistNo) throws SQLException {
        int cnt,albumNo;
        String artist,title,releaseDate;
        ArrayList<Integer> artistNumbers = new ArrayList<>();

        System.out.println("\n\n============================ Add Album ============================");
        System.out.print("Album Title: ");
        title = inFromUser.nextLine();
        System.out.print("Album's release Date (Format = yyyy-mm-dd): ");
        releaseDate = inFromUser.nextLine();
        sql = "insert into album(title,releaseDate) values('" + title + "','" + releaseDate + "')";
        getSqlResult(sql);

        sql = "select max(albumNo) from album";
        rs = getSqlResult(sql);
        rs.next();
        albumNo = rs.getInt(1);
        if (artistNo == -1) {
            while (true) {
                cnt = 0;
                System.out.print("Artist (If you want to stop, Enter q): ");
                artist = inFromUser.nextLine();
                if(artist.equals("q")) break;
                sql = "select * from artist where name like '%" + artist + "%'";
                rs = getSqlResult(sql);
                while (rs.next()) cnt++;
                if (cnt == 0) System.out.println("no such artist exist. Try again.");
                else {
                    cnt = 0;
                    while (rs.previous()) ;
                    while (rs.next()) {
                        cnt++;
                        System.out.println(cnt + ". " + rs.getString(3) + "      " + rs.getDate(2));
                    }
                    System.out.print("Select artist: ");
                    int tmp = inFromUser.nextInt();
                    inFromUser.nextLine();
                    for (int i = 0; i < cnt - tmp + 1; i++) rs.previous();
                    artistNumbers.add(rs.getInt(1));
                }
            }
        }
        else artistNumbers.add(artistNo);
        for(int i=0; i<artistNumbers.size(); i++){
            sql = "insert into make_album values("+artistNumbers.get(i)+","+albumNo+")";
            getSqlResult(sql);
        }
    }

    private static void addArtist() throws SQLException {
        String name, date;
        String[] members;

        System.out.println("\n\n============================= Add Artist ==========================");
        System.out.print("Name: ");
        name = inFromUser.nextLine();
        System.out.print("Debut date (Format = yyyy-mm-dd): ");
        date = inFromUser.nextLine();
        sql = "insert into artist(name,debutDate) values('" + name + "','" + date + "')";
        getSqlResult(sql);

        sql = "select max(artistNo) from artist";
        rs = getSqlResult(sql);
        rs.next();
        int artistNo = rs.getInt(1);
        System.out.println("Member list (separate by comma(,))");
        members = inFromUser.nextLine().split(",");
        for(int i=0; i<members.length; i++){
            sql = "insert into artist_member values("+artistNo+",'"+members[i]+"')";
            getSqlResult(sql);
        }
    }

    private static String addPlaylist() throws SQLException {
        String title, open;
        int isOpen;
        System.out.println("\n\n========================= Add Playlist ============================");

        System.out.print("Title: ");
        title = inFromUser.nextLine();
        System.out.print("Private(open for only you) or Public(open for everyone) : ");
        while(true) {
            open = inFromUser.nextLine();
            if(open.equals("Private")){
                isOpen=0;
                break;
            }
            else if(open.equals("Public")){
                isOpen=1;
                break;
            }
            else System.out.println("Please enter 'Private' or 'Public'");
        }
        sql = "insert into playlist values('"+title+"', "+currentUserNo+","+isOpen+")";
        getSqlResult(sql);
        return title;
    }

    /************************ Edit *************************/

    private static void editMusic(int musicNo) throws SQLException {
        int command;
        String query;
        boolean exit = false;
        while(!exit) {
            System.out.println("\n\n============================ Edit Music ===========================");
            System.out.println("1. Title\n2. Time\n3. Genre\n4. Release Date\n5. Lyrics \n6. Go Back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();
            switch (command){
                case 1:
                    System.out.print("Input new Title: ");
                    query = inFromUser.nextLine();
                    sql = "update music set title='"+query+"' where musicNo="+musicNo;
                    getSqlResult(sql);
                    break;
                case 2:
                    System.out.print("Input new Time (Format = mm:ss): ");
                    query = inFromUser.nextLine();
                    sql = "update music set playtime='00:"+query+"' where musicNo="+musicNo;
                    getSqlResult(sql);
                    break;
                case 3:
                    System.out.print("Input new Genre: ");
                    query = inFromUser.nextLine();
                    sql = "update music set genre='"+query+"' where musicNo="+musicNo;
                    getSqlResult(sql);
                    break;
                case 4:
                    System.out.print("Input new Release Date (Format = yyyy-mm-dd): ");
                    query = inFromUser.nextLine();
                    sql = "update music set releaseDate='"+query+"' where musicNo="+musicNo;
                    getSqlResult(sql);
                    break;
                case 5:
                    String tmp;
                    query="";
                    System.out.println("Input new Lyrics (if want to stop input, enter 'q') ");
                    while(true) {
                        tmp = inFromUser.nextLine();
                        if(tmp.equals("q")) break;
                        query+=(tmp+"\n");
                    }
                    sql = "update music set lyrics='"+query+"' where musicNo="+musicNo;
                    getSqlResult(sql);
                case 6:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void editUser(int userNo, int isMy) throws SQLException {
        int command;
        boolean exit = false;
        String query, user;

        if(manager &&isMy==1) user = "manager";
        else user="user";

        while(!exit) {
            if(isMy==1) System.out.println("\n\n============================ Edit My Info ==========================");
            else System.out.println("\n\n=========================== Edit User Info ==========================");
            System.out.println("1. Name\n2. Password\n3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();
            switch (command){
                case 1:
                    System.out.print("Input new Name: ");
                    query = inFromUser.nextLine();
                    sql = "update "+user+" set name='"+query+"' where "+user+"No="+userNo;
                    getSqlResult(sql);
                    break;
                case 2:
                    while(true) {
                        System.out.print("Input new Password: ");
                        query = inFromUser.nextLine();
                        System.out.print("Input new Password Again: ");
                        String tmp = inFromUser.nextLine();
                        if (query.equals(tmp)) {
                            sql = "update " + user + " set password='" + query + "' where " + user + "No=" + userNo;
                            getSqlResult(sql);
                            break;
                        } else {
                            System.out.println("Enter Password Again.");
                        }
                    }
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void editAlbum(int albumNo) throws SQLException {
        int command;
        boolean exit =false;
        String query;

        while(!exit) {
            System.out.println("\n\n========================== Edit Album =============================");
            System.out.println("1. Title");
            System.out.println("2. Release Date");
            System.out.println("3. Go back");
            System.out.println("-------------------------------------------------------------------");
            System.out.print("Input command: ");
            command = inFromUser.nextInt();
            inFromUser.nextLine();

            switch (command){
                case 1:
                    System.out.print("Input new title: ");
                    query = inFromUser.nextLine();
                    sql = "update album set title='"+query+"' where albumNo="+albumNo;
                    getSqlResult(sql);
                    exit=true;
                    break;
                case 2:
                    System.out.print("Input new release date: ");
                    query = inFromUser.nextLine();
                    sql = "update album set releaseDate='"+query+"' where albumNo="+albumNo;
                    getSqlResult(sql);
                case 3:
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong command.");
                    break;
            }
        }
    }

    private static void addToPlayList(int musicNo) throws SQLException {
        int cnt=0, playlistNo;
        String title=null;

        System.out.println("\n\n====================== Add Music to PlayList ======================");

        sql="select * from playlist where userNo="+currentUserNo;
        rs=getSqlResult(sql);
        while(rs.next()){
            cnt++;
            System.out.println(cnt+". "+rs.getString(1));
        }
        if(cnt==0){
            System.out.println("No playlist exist.");
            System.out.println("\n1. Add playlist");
            System.out.println("2. Go back");
        }
        System.out.println("-------------------------------------------------------------------");
        if(cnt==0){
            int cmd = inFromUser.nextInt();
            inFromUser.nextLine();
            if(cmd ==1) title = addPlaylist();
            else if (cmd==2) return;
        }
        else {
            System.out.print("Select playlist: ");
            playlistNo = inFromUser.nextInt();
            inFromUser.nextLine();
            for (int i = 0; i < cnt - playlistNo + 1; i++) rs.previous();
            title = rs.getString(1);
        }
        sql = "select count(*) from add_to_playlist where musicNo="+musicNo+" and userNo="+currentUserNo+" and title='"+title+"'";
        rs = getSqlResult(sql);
        rs.next();
        if(rs.getInt(1)==1){
            System.out.println("It is already in this playlist.");
            System.out.println("Enter to go back");
            inFromUser.nextLine();
        }
        else {
            sql = "insert into add_to_playlist values(" + musicNo + "," + currentUserNo + ",'" + title + "')";
            getSqlResult(sql);
        }
    }

    /*************************** Delete **********************************/

    private static void deleteUser(int userNo) throws SQLException {
        sql = "delete from comment where userNo="+userNo;
        getSqlResult(sql);
        sql = "delete from play where userNo="+userNo;
        getSqlResult(sql);
        sql = "delete from likes where userNo="+userNo;
        getSqlResult(sql);
        sql = "delete from add_to_playlist where userNo="+userNo;
        getSqlResult(sql);
        sql = "delete from playlist where userNo="+userNo;
        getSqlResult(sql);
        sql = "delete from user where userNo="+userNo;
        getSqlResult(sql);
    }


    private static int deleteManager() throws SQLException {
        int tmp;
        sql = "select managerNo from manager";
        rs = getSqlResult(sql);
        ArrayList<Integer> managerNo = new ArrayList<>();
        while(rs.next()) {
            tmp=rs.getInt(1);
            if(tmp!=currentUserNo) managerNo.add(tmp);
        }
        if(managerNo.size()==0){
            System.out.println("You are Last manager. Can't Withdrawal.");
            return 0;
        }
        sql = "update music set managerNo="+managerNo.get((int)(Math.random() * managerNo.size()))+" where managerNo="+currentUserNo;
        getSqlResult(sql);
        sql = "update user set managerNo="+managerNo.get((int)(Math.random() * managerNo.size()))+" where managerNo="+currentUserNo;
        getSqlResult(sql);
        sql = "delete from manager where managerNo="+currentUserNo;
        getSqlResult(sql);
        return 1;
    }

    private static void deleteMusic(int musicNo) throws SQLException {
        sql = "delete from make_music where musicNo="+musicNo;
        getSqlResult(sql);
        sql = "delete from comment where musicNo="+musicNo;
        getSqlResult(sql);
        sql = "delete from play where musicNo="+musicNo;
        getSqlResult(sql);
        sql = "delete from likes where musicNo="+musicNo;
        getSqlResult(sql);
        sql = "delete from add_to_playlist where musicNo="+musicNo;
        getSqlResult(sql);
        sql = "delete from music where musicNo="+musicNo;
        getSqlResult(sql);
    }

    private static void deleteArtist(int artistNo) throws SQLException {
        sql = "select music.musicNo from music, make_music where make_music.musicNo=music.musicNo and artistNo="+artistNo;
        rs=getSqlResult(sql);
        while(rs.next()){
            deleteMusic(rs.getInt(1));
        }
        sql = "select album.albumNo from make_album, album where make_album.albumNo = album.albumNo and artistNo="+artistNo;
        rs = getSqlResult(sql);
        while(rs.next()){
            deleteAlbum(rs.getInt(1));
        }
        sql = "delete from artist_member where artistNo="+artistNo;
        getSqlResult(sql);
        sql = "delete from artist where artistNo="+artistNo;
        getSqlResult(sql);
    }

    private static void deleteAlbum(int albumNo) throws SQLException {
        sql = "update music set albumNo = null where albumNo="+albumNo;
        getSqlResult(sql);
        sql = "delete from make_album where albumNo="+albumNo;
        getSqlResult(sql);
        sql = "delete from album where albumNo="+albumNo;
        getSqlResult(sql);
    }

    private static void deletePlaylist(String title) throws SQLException {
        sql = "delete from add_to_playlist where title='"+title+"' and userNo="+currentUserNo;
        getSqlResult(sql);
        sql = "delete from playlist where title='"+title+"' and userNo="+currentUserNo;
        getSqlResult(sql);
    }
}
