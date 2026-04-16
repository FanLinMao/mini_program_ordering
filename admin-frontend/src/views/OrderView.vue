<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">订单管理</h1>
        <p class="page-desc">聚焦待支付、待取餐、待评价和退款售后等订单状态管理。</p>
      </div>
      <el-button type="warning" @click="openCreateDialog">新增订单</el-button>
    </div>

    <section class="panel-card section-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="全部订单" name="all" />
        <el-tab-pane label="待支付" name="待支付" />
        <el-tab-pane label="待取餐" name="待取餐" />
        <el-tab-pane label="待评价" name="待评价" />
        <el-tab-pane label="退款售后" name="退款售后" />
      </el-tabs>

      <el-table v-loading="loading" :data="filteredRows" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" min-width="220" />
        <el-table-column prop="contactName" label="用户" width="120" />
        <el-table-column prop="shopName" label="门店" min-width="180" />
        <el-table-column prop="amountText" label="金额" width="120" />
        <el-table-column prop="statusText" label="状态" width="120" />
        <el-table-column prop="payTypeText" label="支付方式" width="120" />
        <el-table-column prop="createTimeText" label="下单时间" width="180" />
        <el-table-column label="操作" width="280" header-align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetailDialog(row)">查看详情</el-button>
            <el-button link type="warning" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && filteredRows.length === 0" class="empty-tip">
        当前筛选条件下暂无订单数据
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑订单' : '新增订单'" width="620px">
      <el-form label-position="top">
        <el-form-item label="订单号">
          <el-input v-model="form.orderNo" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="form.userId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="门店名称">
          <el-input v-model="form.shopName" />
        </el-form-item>
        <el-form-item label="取餐方式">
          <el-input v-model="form.pickupType" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contactName" />
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态编码">
          <el-input v-model="form.status" />
        </el-form-item>
        <el-form-item label="状态文案">
          <el-input v-model="form.statusText" />
        </el-form-item>
        <el-form-item label="提示文案">
          <el-input v-model="form.note" />
        </el-form-item>
        <el-form-item label="订单类型">
          <el-input v-model="form.orderType" />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="form.payType" style="width: 100%">
            <el-option label="微信支付" value="wxpay" />
            <el-option label="现炒下单" value="cookNow" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="warning" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="订单详情" width="920px">
      <div v-loading="detailLoading" class="detail-wrapper">
        <template v-if="detail">
          <el-descriptions :column="2" border class="detail-descriptions">
            <el-descriptions-item label="订单号">{{ detail.orderNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">{{ detail.statusText || '-' }}</el-descriptions-item>
            <el-descriptions-item label="门店名称">{{ detail.shopName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系人">{{ detail.contactName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ detail.userId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="取餐方式">{{ detail.pickupType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="订单类型">{{ detail.orderTypeText || '-' }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ detail.payTypeText || '-' }}</el-descriptions-item>
            <el-descriptions-item label="下单时间">{{ detail.createTimeText || '-' }}</el-descriptions-item>
            <el-descriptions-item label="最后更新">{{ detail.updateTimeText || '-' }}</el-descriptions-item>
            <el-descriptions-item label="提示文案" :span="2">{{ detail.note || '-' }}</el-descriptions-item>
          </el-descriptions>

          <div class="detail-section">
            <div class="detail-section-title">菜品明细</div>
            <el-table :data="detail.items" style="width: 100%">
              <el-table-column prop="dishName" label="菜品名称" min-width="220" />
              <el-table-column prop="priceText" label="单价" width="120" />
              <el-table-column prop="count" label="数量" width="100" />
              <el-table-column prop="amountText" label="小计" width="120" />
            </el-table>
            <div class="detail-total">
              合计金额：<span>{{ detail.amountText || '¥0.00' }}</span>
            </div>
          </div>
        </template>

        <div v-else class="empty-tip">当前订单暂无可展示的详情信息</div>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createOrder,
  deleteOrder,
  fetchOrderDetail,
  fetchOrderList,
  updateOrder
} from '../api/order'

const loading = ref(false)
const detailLoading = ref(false)
const activeTab = ref('all')
const rows = ref([])
const detail = ref(null)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = reactive({
  orderNo: '',
  userId: 1,
  shopName: '私人厨房',
  pickupType: '到店自取',
  contactName: '',
  amount: 0,
  status: 'pendingPay',
  statusText: '待支付',
  note: '',
  orderType: 'miniapp',
  payType: 'wxpay'
})

const payTypeMap = {
  wxpay: '微信支付',
  cookNow: '现炒下单'
}

const orderTypeMap = {
  miniapp: '小程序订单',
  admin: '后台录入',
  dineIn: '堂食订单'
}

const filteredRows = computed(() => {
  if (activeTab.value === 'all') return rows.value
  return rows.value.filter((item) => item.statusText === activeTab.value)
})

const formatAmountText = (amount) => `¥${Number(amount || 0).toFixed(2)}`

const formatDateTime = (value) => (value ? String(value).replace('T', ' ') : '-')

const resetForm = () => {
  editingId.value = null
  form.orderNo = ''
  form.userId = 1
  form.shopName = '私人厨房'
  form.pickupType = '到店自取'
  form.contactName = ''
  form.amount = 0
  form.status = 'pendingPay'
  form.statusText = '待支付'
  form.note = ''
  form.orderType = 'miniapp'
  form.payType = 'wxpay'
}

const mapRows = (data) => {
  rows.value = data.map((item) => ({
    ...item,
    amountText: formatAmountText(item.amount),
    payTypeText: payTypeMap[item.payType] || item.payType || '-',
    createTimeText: formatDateTime(item.createTime)
  }))
}

const normalizeDetail = (data) => ({
  ...data,
  amountText: formatAmountText(data?.amount),
  payTypeText: payTypeMap[data?.payType] || data?.payType || '-',
  orderTypeText: orderTypeMap[data?.orderType] || data?.orderType || '-',
  createTimeText: formatDateTime(data?.createTime),
  updateTimeText: formatDateTime(data?.updateTime),
  items: Array.isArray(data?.items)
    ? data.items.map((item) => ({
        ...item,
        priceText: formatAmountText(item.price),
        amountText: formatAmountText(item.amount)
      }))
    : []
})

const loadOrders = async () => {
  loading.value = true
  try {
    mapRows(await fetchOrderList())
  } catch (error) {
    ElMessage.error(`订单数据加载失败：${error.message}`)
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  isEdit.value = true
  editingId.value = row.id
  form.orderNo = row.orderNo
  form.userId = row.userId
  form.shopName = row.shopName
  form.pickupType = row.pickupType
  form.contactName = row.contactName
  form.amount = Number(row.amount || 0)
  form.status = row.status
  form.statusText = row.statusText
  form.note = row.note || ''
  form.orderType = row.orderType || 'miniapp'
  form.payType = row.payType || 'wxpay'
  dialogVisible.value = true
}

const openDetailDialog = async (row) => {
  detailDialogVisible.value = true
  detailLoading.value = true
  detail.value = null
  try {
    const result = await fetchOrderDetail(row.id)
    detail.value = result ? normalizeDetail(result) : null
  } catch (error) {
    ElMessage.error(`订单详情加载失败：${error.message}`)
  } finally {
    detailLoading.value = false
  }
}

const handleSubmit = async () => {
  try {
    const payload = { ...form }
    if (isEdit.value) {
      await updateOrder(editingId.value, payload)
      ElMessage.success('订单更新成功')
    } else {
      await createOrder(payload)
      ElMessage.success('订单新增成功')
    }
    dialogVisible.value = false
    await loadOrders()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除订单“${row.orderNo}”吗？`, '删除确认', {
      type: 'warning'
    })
    await deleteOrder(row.id)
    ElMessage.success('订单删除成功')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败：${error.message}`)
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.section-card {
  padding: 18px;
}

.detail-wrapper {
  min-height: 220px;
}

.detail-descriptions {
  margin-bottom: 20px;
}

.detail-section {
  margin-top: 8px;
}

.detail-section-title {
  margin-bottom: 12px;
  color: #2d3648;
  font-size: 16px;
  font-weight: 700;
}

.detail-total {
  margin-top: 14px;
  color: #6d778b;
  font-size: 14px;
  text-align: right;
}

.detail-total span {
  color: #2d3648;
  font-size: 18px;
  font-weight: 700;
}

.empty-tip {
  padding: 18px 0 4px;
  color: #8a96aa;
  font-size: 14px;
  text-align: center;
}
</style>
