package com.lovi.um.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.lovi.puppy.annotation.Controller;
import com.lovi.puppy.annotation.ModelAttribute;
import com.lovi.puppy.annotation.PathVariable;
import com.lovi.puppy.annotation.RequestMapping;
import com.lovi.puppy.annotation.ResponseBody;
import com.lovi.puppy.annotation.enums.HttpMethod;
import com.lovi.puppy.exceptions.ServiceCallerException;
import com.lovi.puppy.future.HttpResponseResult;
import com.lovi.puppy.message.FailResult;
import com.lovi.puppy.message.Result;
import com.lovi.puppy.message.ServiceCaller;
import com.lovi.um.common.ResponseMessage;
import com.lovi.um.model.User;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private ServiceCaller serviceCaller;
	
	@ResponseBody
	@RequestMapping(produce="application/json")
	public void findAll(HttpResponseResult responseResult) throws ServiceCallerException{
		
		Result<List<User>> result = Result.create();
		FailResult failResult = FailResult.create();
		
		serviceCaller.call("UserService.findAll", result);
		
		result.process(r->{
			responseResult.complete(new ResponseMessage(1, r));
		}, failResult);
		
		failResult.setHandler(fail->{
			responseResult.complete(new ResponseMessage(-1, fail.getMessage()),500);
		});
	}
	
	@ResponseBody
	@RequestMapping(value="/:userId",produce="application/json")
	public void findByUserId(@PathVariable("userId") String id, HttpResponseResult responseResult) throws ServiceCallerException{
		
		Result<User> result = Result.create();
		FailResult failResult = FailResult.create();
		
		serviceCaller.call("UserService.findByUserId", result, id);
		
		result.process(r->{
			responseResult.complete(new ResponseMessage(1, r));
		}, failResult);
		
		failResult.setHandler(fail->{
			responseResult.complete(new ResponseMessage(-1, fail.getMessage()),500);
		});
	}
	
	@ResponseBody
	@RequestMapping(method=HttpMethod.POST,produce="application/json")
	public void insert(@ModelAttribute User user,HttpResponseResult responseResult) throws ServiceCallerException{
		
		serviceCaller.call("UserService.insert", user);
		responseResult.complete(new ResponseMessage(1, "do insert"),200);
	
	}
}
