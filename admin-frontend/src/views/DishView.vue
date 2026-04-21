<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">菜品管理</h1>
        <p class="page-desc">围绕分类、价格、图片、推荐文案和上下架状态统一维护。</p>
      </div>
      <div class="header-actions">
        <el-button @click="loadDishes">刷新</el-button>
        <el-button type="warning" @click="openCreateDialog">新增菜品</el-button>
      </div>
    </div>

    <section class="panel-card table-card">
      <el-table v-loading="loading" :data="rows" style="width: 100%">
        <el-table-column label="图片" width="150" align="center">
          <template #default="{ row }">
            <div class="dish-image-cell">
              <el-image
                v-if="row.image"
                :src="row.image"
                fit="cover"
                class="dish-thumb"
                :preview-src-list="[row.image]"
                preview-teleported
              />
              <span v-else class="empty-image-text">暂无图片</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="菜品名称" width="150" />
        <el-table-column prop="categoryName" label="所属分类" width="140" />
        <el-table-column prop="priceText" label="售价" width="120" />
        <el-table-column prop="monthlySales" label="月售" width="120" />
        <el-table-column prop="statusText" label="状态" width="120" />
        <el-table-column prop="extra" label="推荐文案" min-width="220" />
        <el-table-column label="操作" width="220" header-align="left">
          <template #default="{ row }">
            <el-button link type="warning" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑菜品' : '新增菜品'" width="560px">
      <el-form label-position="top">
        <el-form-item label="所属分类">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="菜品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="售价">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="月售">
          <el-input-number v-model="form.monthlySales" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="推荐值">
          <el-input-number v-model="form.praise" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="推荐文案">
          <el-input v-model="form.extra" />
        </el-form-item>
        <el-form-item label="菜单图片">
          <div class="upload-field">
            <div class="upload-actions">
              <el-upload
                :show-file-list="false"
                :http-request="handleImageUpload"
                accept="image/png,image/jpeg,image/jpg,image/gif,image/webp"
              >
                <el-button type="warning" plain :loading="uploading">上传图片</el-button>
              </el-upload>
              <el-button v-if="form.image" link type="danger" @click="clearImage">清空图片</el-button>
            </div>
            <el-input
              :model-value="form.image"
              readonly
              placeholder="上传成功后会自动回填图片地址"
            />
          </div>
        </el-form-item>
        <el-form-item label="图片预览">
          <div class="image-preview-wrap">
            <el-image
              v-if="form.image"
              :src="form.image"
              fit="cover"
              class="dialog-preview-image"
              :preview-src-list="[form.image]"
              preview-teleported
            />
            <div v-else class="image-preview-empty">上传菜单图片后，这里会显示预览。</div>
          </div>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="上架中" :value="1" />
            <el-option label="已下架" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="warning" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fetchCategoryList } from '../api/category'
import { createDish, deleteDish, fetchDishList, updateDish, uploadDishImage } from '../api/dish'

const loading = ref(false)
const uploading = ref(false)
const rows = ref([])
const categories = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = reactive({
  categoryId: undefined,
  name: '',
  price: 0,
  image: '',
  monthlySales: 0,
  praise: 0,
  extra: '',
  sort: 0,
  status: 1
})

const categoryNameMap = () =>
  categories.value.reduce((acc, item) => {
    acc[item.id] = item.name
    return acc
  }, {})

const resetForm = () => {
  editingId.value = null
  form.categoryId = undefined
  form.name = ''
  form.price = 0
  form.image = ''
  form.monthlySales = 0
  form.praise = 0
  form.extra = ''
  form.sort = 0
  form.status = 1
}

const mapRows = (dishes) => {
  const nameMap = categoryNameMap()
  rows.value = dishes.map((item) => ({
    ...item,
    categoryName: nameMap[item.categoryId] || `分类#${item.categoryId}`,
    priceText: `¥${Number(item.price || 0).toFixed(2)}`,
    statusText: Number(item.status) === 1 ? '上架中' : '已下架',
    extra: item.extra || '-',
    image: item.image || ''
  }))
}

const loadDishes = async () => {
  loading.value = true
  try {
    const [categoryData, dishData] = await Promise.all([fetchCategoryList(), fetchDishList()])
    categories.value = categoryData
    mapRows(dishData)
  } catch (error) {
    ElMessage.error(`菜品数据加载失败：${error.message}`)
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
  form.categoryId = row.categoryId
  form.name = row.name
  form.price = Number(row.price || 0)
  form.image = row.image || ''
  form.monthlySales = row.monthlySales || 0
  form.praise = row.praise || 0
  form.extra = row.extra === '-' ? '' : row.extra
  form.sort = row.sort || 0
  form.status = row.status
  dialogVisible.value = true
}

const handleImageUpload = async (options) => {
  uploading.value = true
  try {
    const result = await uploadDishImage(options.file)
    form.image = result.url || ''
    ElMessage.success('图片上传成功')
    options.onSuccess?.(result)
  } catch (error) {
    ElMessage.error(`图片上传失败：${error.message}`)
    options.onError?.(error)
  } finally {
    uploading.value = false
  }
}

const clearImage = () => {
  form.image = ''
}

const handleSubmit = async () => {
  try {
    const payload = {
      categoryId: form.categoryId,
      name: form.name,
      price: form.price,
      image: form.image,
      monthlySales: form.monthlySales,
      praise: form.praise,
      extra: form.extra,
      sort: form.sort,
      status: form.status
    }

    if (isEdit.value) {
      await updateDish(editingId.value, payload)
      ElMessage.success('菜品更新成功')
    } else {
      await createDish(payload)
      ElMessage.success('菜品新增成功')
    }

    dialogVisible.value = false
    await loadDishes()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除菜品“${row.name}”吗？`, '删除确认', {
      type: 'warning'
    })
    await deleteDish(row.id)
    ElMessage.success('菜品删除成功')
    await loadDishes()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败：${error.message}`)
    }
  }
}

onMounted(() => {
  loadDishes()
})
</script>

<style scoped>
.header-actions {
  display: flex;
  gap: 12px;
}

.table-card {
  padding: 18px;
}

.dish-image-cell {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 56px;
}

.dish-thumb {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  background: #f5f7fa;
}

.empty-image-text {
  font-size: 12px;
  color: #909399;
}

.upload-field {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.upload-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.image-preview-wrap {
  width: 100%;
  min-height: 160px;
  border: 1px dashed #dcdfe6;
  border-radius: 16px;
  background: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.dialog-preview-image {
  width: 100%;
  height: 220px;
}

.image-preview-empty {
  font-size: 13px;
  color: #909399;
}
</style>
