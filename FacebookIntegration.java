package abc;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
public class FacebookIntegration
{
	public static float arr[]=new float[3];
		
	//========================code for fetching comments from facebook===================
	
	public float[] ex()
	{
		return arr;
	}
	public static void fun1(String searchPost)throws FacebookException, IOException
	{
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("1553578874968576","bfdeb8f81a52dedc962e7557ccde8665");
		//facebook.setOAuthAppId("869609219804513","08caab148d7ae44fd7b17ea609a43dbb");
		facebook.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic,off"); 
		
		AccessToken accessToken=new AccessToken("EAAWEZBMT5GgABAON9OwZCTBXQsZCeFlKSlS7LsfK7UesnvBSmcTLXGipc7Hgp6wWDZBeBMuIxj8Rh2cblXZCwZAm2IaQwZApViu8BgsCsy7nMZBbQ9jq4gmw5VLY7HH5Mmy5cbYNnEmO27naljBEqO06re30On1DsXUZD");
		//AccessToken accessToken=new AccessToken("EAAMW56yjzWEBAF5oDdPXDBH6SGWAmQmZAVSDSAaPD13qyKmZCMV4TGN2TsxqBLsaQOBSY8gY1CVtF5JFw0gWH0wS12U793O6AU4TZAgkcFaLYvQJdlf66yvbjPZCDDmrNrRosyOPEPWmIC0g7SjJbIEjyBxmy0ZAz2VOFVQDALwZDZD");
		facebook.setOAuthAccessToken(accessToken);		
		
		String results=""; 
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd_MM_yyyy_hh_mm");
		//String fileName = "C:\\files\\" + searchPost
		//		+ "_" + simpleDateFormat.format(date) + ".txt";
		String fileName = "" + searchPost
				+ "_" + simpleDateFormat.format(date) + ".txt";
		//C:\Users\saura\workspace\www\src\abc
		
		File file = new File(fileName);
		try{
		results= getFacebookPostes(facebook, searchPost);
		
		if(results!=null)
		{
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println(results);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println(results);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println(results);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println(results);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			System.out.println(results);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
		}
		if(results==null)
		{
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
			System.out.println("!!!!!!!!!!!!!!!!No post retrieved");
		}
		else
		{
		
		//Write into file
			if (!file.exists()) 
			{
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(results);
				bw.close();
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				System.out.println("Completed Facebook posts");
			}	
		}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Posts not able to retrieve!!! Please try a different brand.");
		}
		//twitter
		//String fileName="";
		if (!file.exists()) 
			file.createNewFile();
		TwitterTweets tt=new TwitterTweets();
		try
		{
		tt.tweet(fileName,searchPost);
		System.out.println("Completed");	
		}
		catch(Exception e){
			System.out.println("Tweets not able to retrieve!!! Please try a different brand.");
		}
		
		
		PreProcess nn= new PreProcess();
		
		try 
		{
			System.out.println("Start neural");
			
		//	File fr3=new File("C:/Project/Pattern.txt");
			//File fr3=new File(fileName);
			arr=nn.start(file);
			System.out.println(arr[0]);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// This method is used to get Facebook posts based on the search string set above
	public static String getFacebookPostes(Facebook Facebook, String searchPost) throws FacebookException 
	{
		String searchResult = "Item : " + searchPost + "\n\n";
		StringBuffer searchMessage = new StringBuffer();
		ResponseList<Post> results = Facebook.getFeed(searchPost,new Reading().limit(100));
		//ResponseList<Post> results = Facebook.searchPosts(searchPost);
		if(results.isEmpty())
		{
			return "";
		}
		for (Post post : results) 
		{
			System.out.println(post.getMessage());
			searchMessage.append(post.getMessage() + "\n");
			for (int j = 0; j < post.getComments().size(); j++) 
			{
				System.out.println("\n"+post.getComments().get(j).getMessage());
				searchMessage.append(post.getComments().get(j).getMessage()
						+ ", ");
				
				System.out.println("\n------------");
			}
			System.out.println("\n===============");
		}
		
		searchResult = searchResult + searchMessage.toString();
		
		return searchResult;
	}	
}
	
	



