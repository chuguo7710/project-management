package com.example.modules.task.mapper;

import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;
import com.example.modules.task.entity.TaskValid;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {

    /** 获取任务校验数据 */
    @Select("""
            SELECT p.leader_id AS projectLeaderId, t.creator_id AS taskCreatorId, t.assignee_id AS taskAssigneeId
            FROM tasks AS t
            LEFT JOIN projects AS p ON p.project_id = t.project_id
            WHERE t.task_id = #{taskId}
            """)
    TaskValid selectTaskValidByTaskId(@Param("taskId") Long taskId);

    /** 根据项目ID查询任务列表 */
    @Select("""
            SELECT t.task_id, t.title, t.description,  t.status, t.priority, t.deadline,
                   t.project_id, p.project_name, t.assignee_id, a.username AS assignee_name,
                   t.creator_id, t.creator_name, t.completed_at, t.create_time
            FROM tasks AS t
            LEFT JOIN accounts AS a ON a.user_id = t.assignee_id
            LEFT JOIN projects AS p ON p.project_id = t.project_id
            WHERE t.project_id = #{projectId}
            ORDER BY t.deadline IS NULL, t.deadline
            """)
    List<Task> selectTaskListByProjectId(@Param("projectId") Long projectId);

    /** 创建任务 */
    @Insert("""
            INSERT INTO tasks (title, description, status, priority, deadline, project_id, assignee_id, creator_id, creator_name)
            VALUES (#{title}, #{description}, #{status}, #{priority}, #{deadline}, #{projectId}, #{assigneeId}, #{creatorId}, #{creatorName})
            """)
    int insertTask(Task task);

    /** 删除任务 */
    @Delete("""
            DELETE FROM tasks
            WHERE task_id = #{taskId}
            """)
    int deleteTaskByTaskId(@Param("taskId") Long taskId);

    /** 更新任务状态 */
    @Update(" UPDATE tasks SET status = #{status} WHERE task_id = #{taskId} ")
    int updateTaskStatusByTaskId(@Param("taskId") Long taskId, @Param("status") Integer status);

}
