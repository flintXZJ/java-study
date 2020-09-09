package com.xzj.stu.java.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * http工具类
 * @author zhijunxie
 */
@Slf4j
public class HttpUtil {
	public static String send(String strUrl, String jsonData) {
		CloseableHttpClient httpPost = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String responseText = "";
//		Integer statusCode = 0;
		try {
			HttpPost httpost = new HttpPost(strUrl);
			httpost.addHeader(HTTP.CONTENT_TYPE, "application/json");
			StringEntity se = new StringEntity(jsonData);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpost.setEntity(se);
			response = httpPost.execute(httpost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
	            responseText = EntityUtils.toString(entity);
	        }	            
			response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			log.info("发送时发生异常",e);
        } finally {
            try {
            	if(response != null){
            		response.close();
            	}
            } catch (Exception e) {
            	log.info("response关闭连接异常",e);
            }
        }
		return responseText;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.info("发送GET请求出现异常！" , e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				log.info("关闭输入流时发生异常" , e2);
			}
		}
		return result;
	}


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.info("发送 POST 请求出现异常！" , e);
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				log.info("发送IO异常！" ,ex);
			}
		}
		return result;
	}
}
