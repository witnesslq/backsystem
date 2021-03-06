package operation.controller.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import operation.BaseController;
import operation.exception.XueWenServiceException;
import operation.pojo.pub.QueryModel;
import operation.pojo.tags.TagBean;
import operation.pojo.tags.UserTagBean;
import operation.pojo.user.User;
import operation.service.tags.TagService;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tools.Config;
import tools.PageRequestTools;
import tools.ReponseData;
import tools.ResponseContainer;


@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {

	@Autowired
	private TagService tagService;
	
	/**
	 * 添加用户标签行为
	 * @param request
	 * @return
	 */
	@RequestMapping("create")
	public ResponseContainer create(UserTagBean userTagbean,HttpServletRequest request) {
	
			if(!StringUtil.isBlank(userTagbean.getTagName()) 
					&& !StringUtil.isBlank(userTagbean.getUserId()) 
					&& !StringUtil.isBlank(userTagbean.getUserName())
					&& !StringUtil.isBlank(userTagbean.getItemId())
					&& !StringUtil.isBlank(userTagbean.getItemType()) ){
				try {
					//保存用户打标签行为数据到mongo里
					tagService.saveUserTag(userTagbean);
					//保存用户标签相关数据到redis中
					tagService.saveUserTagToRedis(userTagbean);					
					return addResponse(Config.STATUS_200, Config.MSG_CREATE_200, true,
							Config.RESP_MODE_10, "");
				} catch (XueWenServiceException e) {
					e.printStackTrace();
					return addResponse(e.getCode(), e.getMessage(), false,
							Config.RESP_MODE_10, "");
				} catch (Exception e) {
					e.printStackTrace();
					return addResponse(Config.STATUS_505, Config.MSG_505, false,
							Config.RESP_MODE_10, "");
				}
			}
			else
			{
				return addResponse(Config.STATUS_500, Config.MSG_500, false,
						Config.RESP_MODE_10, "");
			}
	}
	
	
	/**
	 * 
	 * 添加基础Tag
	 * @param request
	 * @return
	 */
	@RequestMapping("initTag")
	public ResponseContainer initTag(TagBean tagbean,HttpServletRequest request) {
	
			if(!StringUtil.isBlank(tagbean.getTagName()) 
					&& !StringUtil.isBlank(tagbean.getTagType())){
				try {
					tagService.saveTag(tagbean);
					
					return addResponse(Config.STATUS_200, Config.MSG_CREATE_200, true,
							Config.RESP_MODE_10, "");
				} catch (XueWenServiceException e) {
					e.printStackTrace();
					return addResponse(e.getCode(), e.getMessage(), false,
							Config.RESP_MODE_10, "");
				} catch (Exception e) {
					e.printStackTrace();
					return addResponse(Config.STATUS_505, Config.MSG_505, false,
							Config.RESP_MODE_10, "");
				}
			}
			else
			{
				return addResponse(Config.STATUS_500, Config.MSG_500, false,
						Config.RESP_MODE_10, "");
			}
	}
	
	
	/**
	 * 
	 * 获取基础Tag
	 * @param request
	 * @return
	 */
	@RequestMapping("getbasetags")
	public ResponseContainer getBaseTags(String tagType,HttpServletRequest request) {
	
			if(!StringUtil.isBlank(tagType)){
				try {
				List<TagBean> tagList =	tagService.findByItemtype(tagType);
					
					return addResponse(Config.STATUS_200, Config.MSG_CREATE_200, tagList,
							Config.RESP_MODE_10, "");
				} catch (XueWenServiceException e) {
					e.printStackTrace();
					return addResponse(e.getCode(), e.getMessage(), false,
							Config.RESP_MODE_10, "");
				} catch (Exception e) {
					e.printStackTrace();
					return addResponse(Config.STATUS_505, Config.MSG_505, false,
							Config.RESP_MODE_10, "");
				}
			}
			else
			{
				return addResponse(Config.STATUS_500, Config.MSG_500, false,
						Config.RESP_MODE_10, "");
			}
	}
	
	
	/**
	 * 
	 * 获取基础Tag
	 * @param request
	 * @return
	 */
	@RequestMapping("getitemsbytag")
	public ResponseContainer getItemsByTag(String tagName,String itemType,QueryModel dm,HttpServletRequest request) {
	
		//根据请求参数封装一个分页信息对象
		   Pageable pageable = PageRequestTools.pageRequesMake(dm);
			if(!StringUtil.isBlank(itemType) && !StringUtil.isBlank(tagName)){
			try {
				
				 ReponseData rsData =tagService.findItemsByTagAndItemType(tagName,itemType,pageable);

					
				return addResponse(Config.STATUS_200, Config.MSG_CREATE_200, rsData,
							Config.RESP_MODE_10, "");
				} catch (XueWenServiceException e) {
					e.printStackTrace();
					return addResponse(e.getCode(), e.getMessage(), false,
							Config.RESP_MODE_10, "");
				} catch (Exception e) {
					e.printStackTrace();
					return addResponse(Config.STATUS_505, Config.MSG_505, false,
							Config.RESP_MODE_10, "");
				}
			}
			else
			{
				return addResponse(Config.STATUS_500, Config.MSG_500, false,
						Config.RESP_MODE_10, "");
			}
	}	
	
	
	/** 
	* @author yangquanliang
	* @Description: 通过前端文本分词匹配标签库，返回匹配成功的标签列表
	* @param @param words
	* @param @param request
	* @param @return
	* @return ResponseContainer
	* @throws 
	*/ 
	@RequestMapping("/tagsbywords")
	public ResponseContainer getTagsByWords(String words,HttpServletRequest request) {

	        try {
	        	List<String> listResult =tagService.getTagsByAnalysis(words);
					return addResponse(Config.STATUS_200, Config.MSG_200, listResult,
							Config.RESP_MODE_10, "");
			}  catch (Exception e) {
				return addResponse(Config.STATUS_505, Config.MSG_505, false,
						Config.RESP_MODE_10, "");
			}	
	}
	/**
	 * 
	 * @Title: getTagsByType
	 * @Description: 通过类型拿到热门标签
	 * @param itemType
	 * @param count
	 * @param request
	 * @return ResponseContainer
	 * @throws
	 */
	@RequestMapping("/getTagsByType")
	public ResponseContainer getTagsByType(String itemType,Integer count,HttpServletRequest request) {

	        try {
	        	String str =tagService.getTagsByType(Config.YXTDOMAIN, itemType, count);
					return addResponse(Config.STATUS_200, Config.MSG_200, str,
							Config.RESP_MODE_10, "");
			}  catch (Exception e) {
				return addResponse(Config.STATUS_505, Config.MSG_505, false,
						Config.RESP_MODE_10, "");
			}	
	}
	
	/**
	 * 
	 * @Title: tagForObj
	 * @Description: 通用类 打标签接口
	 * @param tagNames 标签名称  多个可以用，隔开
	 * @param type tag对象类型 1.用户 2.课程 3.小组 4.话题 5分享
	 * @param itemId 对象Id
	 * @return ResponseContainer
	 * @throws
	 */
	@RequestMapping("setItemTags")
	@ResponseBody
	public ResponseContainer tagForObj(String token,String tagNames,String type,String itemId){
		
		
		try {
			User user=getCurrentUser(token);
			tagService.tagForObj(user, tagNames, type, itemId);
			return addResponse(Config.STATUS_200, Config.MSG_200, true,
					Config.RESP_MODE_10, "");
		} catch (XueWenServiceException e) {
			return addResponse(e.getCode(), e.getMessage(),  false,
					Config.RESP_MODE_10, "");
		}
		
	}
	

}
