package operation.service.group;

import java.util.ArrayList;
import java.util.List;

import operation.pojo.group.MyGroup;
import operation.pojo.group.XueWenGroup;
import operation.repo.group.GroupRepository;
import operation.repo.group.MyGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MyGroupService {
	@Autowired
	public MyGroupRepository myGroupRepo;
	@Autowired
	public GroupRepository GroupRepo;

	public List<String> myGroupIds(String userId) {
		MyGroup mg = myGroupRepo.findByUserId(userId);
		if(mg == null){
			return null;
		}
		return mg.getGroupIds();
	}

	public List<XueWenGroup> myGroups(String userId) {
		MyGroup mg = myGroupRepo.findByUserId(userId);
		if(mg == null){
			return null;
		}
		List<XueWenGroup> groups = new ArrayList<XueWenGroup>();
		for (int i = 0; i < mg.getGroupIds().size(); i++) {
			XueWenGroup group = GroupRepo.findOne(mg.getGroupIds().get(i));
			groups.add(group);
		}

		return groups;
	}
	/**
	 * 添加群组到我的群组
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public MyGroup addMyGroup(String userId,String groupId){
		MyGroup mine = myGroupRepo.findByUserId(userId);
		List<String> groupList = new ArrayList<String>();
		if(mine == null){
			mine = new MyGroup();
			mine.setUserId(userId);
			groupList.add(groupId);
			mine.setGroupIds(groupList);
		}else{
			if(!mine.getGroupIds().contains(groupId)){
				mine.addGroupId(groupId);
			}
		}
		
		
		return myGroupRepo.save(mine);
	}
	/**
	 * 退出群，只是管理员或成员退出，将相应的我的群中去掉该群
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean removeMyGroup(String userId,String groupId){
		MyGroup mine = myGroupRepo.findByUserId(userId);
		mine.removeGroupId(groupId);
		myGroupRepo.save(mine);
		return true;
	}
	/**
	 * 解散群，将该群组里的成员所有群组更新
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean removeAllMemberFromGroup(List<Object> memberId,String groupId){
		for (int i = 0; i < memberId.size(); i++) {
			this.removeMyGroup(memberId.get(i).toString(), groupId);
		}
		return true;
	}
	
}
