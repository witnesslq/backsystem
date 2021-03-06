package operation.repo.box;

import java.util.List;

import operation.exception.XueWenServiceException;
import operation.pojo.box.Box;
import operation.pojo.topics.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class BoxTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;
	public BoxTemplate(){
		super();
	}
	/**
	 * 根据boxpost id  和 sourceId  判断此条数据是否存在
	 * @param boxPostId
	 * @param sourceId
	 * @return
	 * @throws XueWenServiceException
	 */
	public boolean isExiseByBoxPostIdAndSourceId(String boxPostId,String sourceId)throws XueWenServiceException{
		Query query=new Query(Criteria.where("post").is(boxPostId).and("sourceId").is(sourceId));
		return mongoTemplate.exists(query, Box.class);
	}
	/**
	 * 根据根据位置Id返回处于此位置下所有的数据，数据中只有sourceId 节点
	 * @param boxPostId
	 * @return
	 * @throws XueWenServiceException
	 */
	public List<Box> getSourceIdsByBoxPostId(String boxPostId)throws XueWenServiceException{
		Query query=new Query(Criteria.where("post").is(boxPostId));
		query.fields().include("sourceId");
		query.fields().include("sourceType");
		return mongoTemplate.find(query, Box.class);
	}
	
	/**
	 * 根据parentid删除
	 * @param boxPostId
	 * @return
	 * @throws XueWenServiceException
	 */
	public Boolean deleteByParentId(String boxPostId)throws XueWenServiceException{
		Query query=new Query(Criteria.where("post").is(boxPostId));
		
		try {
			mongoTemplate.remove(query, Box.class);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 根据sourceId删除
	 * @param boxPostId
	 * @return
	 * @throws XueWenServiceException
	 */
	public Boolean deleteBysourceId(String sourceId)throws XueWenServiceException{
		Query query=new Query(Criteria.where("sourceId").is(sourceId));
		
		try {
			mongoTemplate.remove(query, Box.class);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 根据sourceIds删除
	 * @param boxPostId
	 * @return
	 * @throws XueWenServiceException
	 */
	public void deleteBySourceIds(List<Object> sourceIds)throws XueWenServiceException{
		mongoTemplate.remove(new Query(Criteria.where("sourceId").in(sourceIds)),Box.class);
	}
	
	
}
