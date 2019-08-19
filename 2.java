
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView loginProc(@ModelAttribute("LoginModel") LoginSessionModel loginModel, BindingResult result, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		// form validation
		// Spring 프레임워크의 Validator 객체를 이용해서 userId와 userPw 입력 여부를 체크
		new LoginValidator().validate(loginModel, result);
		if (result.hasErrors()) {
			mav.setViewName("/board/login");
			return mav;
		}

		// 진단결과
		// 인증시도 회수를 체크하는 로직이 존재하지 않음 
		// 사전대입공격 또는 무작위대입공격을 통해 인증정보 탈취 가능
		
		// 대응방안
		// 인증시도 실패 회수를 체크해서 일정 회수를 초과하면 
		// 일정 시간 동안 인증할 수 없도록 차단
		
		// 파라미터로 전달된 userId와 userPw 값과 일치하는 정보가 존재하는지 조회
		String userId = loginModel.getUserId();
		String userPw = loginModel.getUserPw();
		LoginSessionModel loginCheckResult = service.checkUserId(userId, userPw);

		// 일치하는 정보가 없는 경우 
		// 오류 코드와 함께 login 페이지를 반환
		if (loginCheckResult == null) {
			mav.addObject("userId", userId);
			mav.addObject("errCode", 1);
			mav.setViewName("/board/login");
			return mav;
		} 
		// 일치하는 정보가 있는 경우
		// list.do 페이지로 리다이렉트 
		else {
			session.setAttribute("userId", userId);
			session.setAttribute("userName", loginCheckResult.getUserName());
			mav.setViewName("redirect:/board/list.do");
			return mav;
		}

	}



	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView loginProc(@ModelAttribute("LoginModel") LoginSessionModel loginModel, BindingResult result, HttpSession session) {
		ModelAndView mav = new ModelAndView();

		// form validation
		// Spring 프레임워크의 Validator 객체를 이용해서 userId와 userPw 입력 여부를 체크
		new LoginValidator().validate(loginModel, result);
		if (result.hasErrors()) {
			mav.setViewName("/board/login");
			return mav;
		}

		// 진단결과
		// 인증시도 회수를 체크하는 로직이 존재하지 않음 
		// 사전대입공격 또는 무작위대입공격을 통해 인증정보 탈취 가능
		
		// 대응방안
		// 인증시도 실패 회수를 체크해서 일정 회수를 초과하면 
		// 일정 시간 동안 인증할 수 없도록 차단
		
		// #1 세션(DB)으로부터 인증시도회수 정보를 조회
		// #2 인증실패회수가 5회 이상이고 
		//    마지막으로 실패한 시간이 30초 이내이면 인증거부 메시지와 함께 login 페이지로 이동
		// #3 인증실패 시 인증시도회수를 증가시킨 후 해당 정보를 세션(DB)에 저장
		// #4 인증성공 시 인증시도회수를 리셋
		
		// 파라미터로 전달된 userId와 userPw 값과 일치하는 정보가 존재하는지 조회
		String userId = loginModel.getUserId();
		String userPw = loginModel.getUserPw();
		
		// #1 
		// Ctrl+Shift+o 패키지 설정
		LoginHistory loginVO = (LoginHistory)session.getAttribute("LoginVO");
		if (loginVO == null) {
			loginVO = new LoginHistory();
		}

		// 인증시도(로그인) 실패회수를 조회
		int retry = loginVO.getRetry();
		
		// 마지막으로 인증실패한 시간을 조회
		long lastFailedLogin = loginVO.getLastFailedLogin();
		
		// 현재 시간
		long now = new java.util.Date().getTime();
		
		// #2
		if (retry >= 5 && (now - lastFailedLogin) < (30 * 1000)) {
			mav.addObject("userId", userId);
			mav.addObject("errCode", 10);
			mav.setViewName("/board/login");
			return mav;
		}
		
		
		LoginSessionModel loginCheckResult = service.checkUserId(userId, userPw);

		// 일치하는 정보가 없는 경우 
		// 오류 코드와 함께 login 페이지를 반환
		if (loginCheckResult == null) {
			// #3
			loginVO.setRetry(++retry);
			loginVO.setLastFailedLogin(now);
			session.setAttribute("LoginVO", loginVO);
			
			mav.addObject("userId", userId);
			mav.addObject("errCode", 1);
			mav.setViewName("/board/login");
			return mav;
		} 
		// 일치하는 정보가 있는 경우
		// list.do 페이지로 리다이렉트 
		else {
			// #4
			loginVO.setRetry(0);
			loginVO.setLastSuccessedLogin(now);
			session.setAttribute("LoginVO", loginVO);
			
			session.setAttribute("userId", userId);
			session.setAttribute("userName", loginCheckResult.getUserName());
			mav.setViewName("redirect:/board/list.do");
			return mav;
		}
	}
