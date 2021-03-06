package operation.service.study;

import java.util.List;

import operation.exception.XueWenServiceException;
import operation.pojo.study.Study;
import operation.repo.study.StudyRepository;
import operation.repo.study.StudyTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import tools.Config;
import tools.StringUtil;

@Service
@Component
public class StudyService {

	@Autowired
	private StudyRepository studyRepository;
	@Autowired
	private StudyTemplate studyTemplate;
	
	/**
	 * 用户收藏,不进行是否收藏判断
	 * @param praise
	 * @param user
	 * @throws XueWenServiceException
	 */
	public void addStudyNotCheck(String domain,String appkey,String sourceId,String studyType ,String  userId)throws XueWenServiceException{
		if(StringUtil.isBlank(userId) || StringUtil.isBlank(domain) || StringUtil.isBlank(appkey) || StringUtil.isBlank(sourceId)){
			throw new  XueWenServiceException(Config.STATUS_201,Config.MSG_201,null);
		}
		Study study=new Study();
		study.setDomain(domain);
		study.setAppKey(appkey);
		study.setSourceId(sourceId);
		study.setStudyType(studyType);
		study.setUserId(userId);
		long time=System.currentTimeMillis();
		study.setCtime(time);
		study.setUtime(time);
		studyRepository.save(study);
	}
	
	/**
	 * 用户是否学习
	 * @param userId
	 * @param appKey
	 * @param sourceId
	 * @param type
	 * @return
	 * @throws XueWenServiceException
	 */
	public boolean isUserFav(String userId,String domain,String sourceId,String studyType)throws XueWenServiceException{
		if(StringUtil.isBlank(userId) || StringUtil.isBlank(domain) || StringUtil.isBlank(sourceId)){
			throw new  XueWenServiceException(Config.STATUS_201,Config.MSG_201,null);
		}
		return studyTemplate.existsByUserIdAndDomainAndSourceIdAndStudyType(userId,domain,sourceId,studyType);
	}
	/**
	 * 获得课程收藏用户列表
	 * @param userId
	 * @param groupCourseId
	 * @param domain
	 * @param pageable
	 * @return
	 * @throws XueWenServiceException
	 */
	public Page<Study> getUserStudyedList(String userId, String groupCourseId,String domain, Pageable pageable) throws XueWenServiceException{
		if(StringUtil.isBlank(groupCourseId) || StringUtil.isBlank(userId) || StringUtil.isBlank(domain)){
			throw new  XueWenServiceException(Config.STATUS_201,Config.MSG_201,null);
		}
		return studyRepository.findBySourceIdAndDomain(groupCourseId,domain,pageable);
	}
	/**
	 * 计算某一用户学习的课程数量
	 * @param userId
	 * @return
	 * @throws XueWenServiceException
	 */
	public int getStudyedCountByUser(String userId)throws XueWenServiceException{
		return studyRepository.countByUserIdAndDomainAndStudyType(userId, Config.YXTDOMAIN, Config.TYPE_COURSE_GROUP);
		
	}
	
	/**
	 * 根据来源集合删除学习记录
	 * @param sourceIds
	 * @throws XueWenServiceException
	 */
	public void deleteBySourceIds(List<Object> sourceIds)throws XueWenServiceException{
		studyTemplate.deleteBySourceIds(sourceIds);
	}
	/**
	 * 根据来源集合删除学习记录
	 * @param sourceIds
	 * @throws XueWenServiceException
	 */
	public void deleteByUserIdAndSourceIds(String userId,List<Object> sourceIds)throws XueWenServiceException{
		studyTemplate.deleteByUserIdAndSourceIds(userId,sourceIds);
	}
	/**
	 * 根据来源删除学习记录
	 * @param sourceIds
	 * @throws XueWenServiceException
	 */
	public void deleteBySourceId(String  sourceId)throws XueWenServiceException{
		studyTemplate.deleteBySourceId(sourceId);
	}
	
}
