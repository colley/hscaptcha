package com.hs.hscaptcha.sample;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.captcha.ImageCaptchaHelper;
import com.hs.captcha.service.HsCaptchaService;
import com.hs.captcha.service.impl.DefaultHsImageCaptchaService;

/**
 * Servlet implementation class HscaptchaServlet
 */
public class HscaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	private HsCaptchaService hsCaptchaService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		hsCaptchaService = new DefaultHsImageCaptchaService(180, 100000, 75000);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getSession().getId();
		ImageCaptchaHelper.flushNewCaptchaToResponse(request, response, hsCaptchaService, id);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
