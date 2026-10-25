<script setup lang="ts">
import dayjs from 'dayjs'

import type { ProjectMember } from '@/types/project'

// props
defineProps<{
  member: ProjectMember
  clickCard: boolean
}>()

const emit = defineEmits<{
  toggle: [member: ProjectMember]
  detail: [member: ProjectMember]
  remove: [member: ProjectMember]
}>()

// 派生
const isOwner = (member: ProjectMember): boolean => member.myProjectRole === 1
const roleText = (member: ProjectMember): string => (isOwner(member) ? '负责人' : '成员')
const joinText = (time: string): string => (time ? dayjs(time).format('MM-DD HH:mm') : '-')
</script>
<template>
  <div class="member-card" :class="{ 'card-clicked': clickCard }" @click="emit('toggle', member)">
    <div class="member-main">
      <el-avatar class="member-avatar" :src="member.avatar">
        <el-icon><UserFilled /></el-icon>
      </el-avatar>
      <span class="member-name" :title="member.username">{{ member.username }}</span>
      <span class="member-role" :class="{ owner: isOwner(member) }">{{ roleText(member) }}</span>
      <span class="member-join-time">{{ joinText(member.joinTime) }}</span>
      <el-icon class="member-arrow"><ArrowRightBold /></el-icon>
    </div>
    <div class="member-actions">
      <el-button link size="small" @click.stop="emit('detail', member)">详细</el-button>
      <el-button link size="small" :disabled="isOwner(member)" @click.stop="emit('remove', member)"
        >移除</el-button
      >
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;

.member-card {
  user-select: none;
  position: relative;
  overflow: hidden;
  height: 60px;
  margin-bottom: 8px;
  border: 1px solid $border-color;
  border-radius: 8px;
  cursor: pointer;
  .member-main {
    position: relative;
    z-index: 1;
    background-color: #fff;
    display: flex;
    align-items: center;
    gap: 10px;
    height: 100%;
    padding: 0 12px;
    transition: transform 0.2s;
    .member-name {
      flex: 1;
      min-width: 0;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .member-role {
      flex: 0 0 44px;
      font-size: 12px;
      color: $text-secondary-color;
      &.owner {
        color: $primary-color;
      }
    }
    .member-join-time {
      flex: 0 0 84px;
      text-align: right;
      font-size: 12px;
      color: #d1d1d1;
    }
    .member-arrow {
      flex: 0 0 20px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 12px;
      color: $text-secondary-color;
      transition: transform 0.2s;
    }
  }
  .member-actions {
    position: absolute;
    top: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    width: 110px;
    height: 100%;
    background-color: $background-color;
  }
  &.card-clicked .member-main {
    transform: translateX(-110px);
    .member-arrow {
      transform: rotate(180deg);
    }
  }
}
</style>
