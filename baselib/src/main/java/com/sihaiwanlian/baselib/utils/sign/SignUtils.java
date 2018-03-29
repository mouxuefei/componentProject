package com.sihaiwanlian.baselib.utils.sign;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @Description:签名工具类
 * @author hougang@asiainfo.com
 * @date 2017年8月1日 下午5:45:15
 * @version V1.0
 */
public class SignUtils {

	public static String genPackageSign(String appkey, String appsecret,
			Map<String, String> params) {
		SortedMap<String, String> sort = new TreeMap<String, String>(params);
		StringBuilder sb = new StringBuilder(appkey);
		for (Entry<String, String> entry : sort.entrySet()) {
			sb.append(entry.getKey());
			sb.append('=');
			sb.append(entry.getValue());
		}
		sb.append(appsecret);
		System.out.println(sb.toString());
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toLowerCase();
		return packageSign;
	}

	public static boolean verify(String appkey, String appsecret,
			Map<String, String> params) {
		String requestSigh = params.remove("sign");
		String sign = genPackageSign(appkey, appsecret, params);
		return requestSigh.equals(sign);
	}
	
}
