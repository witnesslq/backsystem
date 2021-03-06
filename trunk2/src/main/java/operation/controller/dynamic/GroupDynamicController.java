package operation.controller.dynamic;

import javax.servlet.http.HttpServletRequest;

import operation.BaseController;
import operation.exception.XueWenServiceException;
import operation.pojo.dynamic.GroupDynamic;
import operation.pojo.pub.QueryModel;
import operation.service.dynamic.GroupDynamicService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tools.Config;
import tools.PageRequestTools;
import tools.ReponseDataTools;
import tools.ResponseContainer;

@RestController
@RequestMapping("/groupdynamic")
@Configuration
public class GroupDynamicController extends BaseController{

	private static Logger logger=Logger.getLogger(GroupDynamicController.class);
	@Autowired
	private GroupDynamicService groupDynamicService;
	
	public GroupDynamicController(){
		super();
	}

	/**
	 * 获取最新动态
	 * @param request
	 * @param dm
	 * @param appKey
	 * @return
	 */
	@RequestMapping("getGroupDynamic")
	public @ResponseBody ResponseContainer getNewGroupDynamic(HttpServletRequest request, QueryModel dm) {
		// 根据请求参数封装一个分页信息对象
		Pageable pageable = PageRequestTools.pageRequesMake(dm);
		try {
			String groupId=request.getParameter("groupId");
			String ctime=request.getParameter("ctime");
			String dynamic=request.getParameter("dynamic");
			Page<GroupDynamic> groupDynamics=groupDynamicService.getDynamic(groupId, pageable, Long.parseLong(ctime),dynamic);
			ReponseDataTools.getClientReponseData(getReponseData(), groupDynamics);
			return addPageResponse(Config.STATUS_200, Config.MSG_200, getReponseData(),Config.RESP_MODE_10, "");
		} catch (XueWenServiceException e) {
			e.printStackTrace();
			return addResponse(e.getCode(), e.getMessage(), false,Config.RESP_MODE_10, "");
		}

	}
	
	/**
	 * 获取最新动态
	 * @param request
	 * @param dm
	 * @param appKey
	 * @return
	 */
	@RequestMapping("getGroupDynamic/v1.3.7")
	public @ResponseBody ResponseContainer getNewGroupDynamic137(HttpServletRequest request, QueryModel dm) {
		// 根据请求参数封装一个分页信息对象
		Pageable pageable = PageRequestTools.pageRequesMake(dm);
		try {
			String groupId=request.getParameter("groupId");
			String ctime=request.getParameter("ctime");
			String dynamic=request.getParameter("dynamic");
			Page<GroupDynamic> groupDynamics=groupDynamicService.getDynamic137(groupId, pageable, Long.parseLong(ctime),dynamic);
			ReponseDataTools.getClientReponseData(getReponseData(), groupDynamics);
			return addPageResponse(Config.STATUS_200, Config.MSG_200, getReponseData(),Config.RESP_MODE_10, "");
		} catch (XueWenServiceException e) {
			e.printStackTrace();
			return addResponse(e.getCode(), e.getMessage(), false,Config.RESP_MODE_10, "");
		}
		
	}
	
	
	@RequestMapping("updateAllOldDynamciLikeCount")
	public @ResponseBody ResponseContainer updateAllOldDynamciLikeCount(HttpServletRequest request, QueryModel dm) {
		// 根据请求参数封装一个分页信息对象
		try {
			groupDynamicService.updateAllOldDynamciLikeCount();
			return addResponse(Config.STATUS_200, Config.MSG_200, true,Config.RESP_MODE_10, "");
		} catch (XueWenServiceException e) {
			e.printStackTrace();
			return addResponse(e.getCode(), e.getMessage(), false,Config.RESP_MODE_10, "");
		}
		
	}
}
