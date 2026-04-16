const viewModules = import.meta.glob('../views/**/*.vue')

const viewEntries = Object.entries(viewModules).map(([path, loader]) => {
  const fileName = path.split('/').pop()?.replace('.vue', '') || ''
  return {
    fileName,
    path,
    loader
  }
})

const viewModuleMap = viewEntries.reduce((acc, item) => {
  acc[item.fileName] = item.loader
  return acc
}, {})

export function resolveViewComponent(viewName) {
  return viewModuleMap[viewName] || null
}

export const availableViewOptions = viewEntries
  .filter((item) => !['LoginView', 'AdminLayout', 'RouteNotFoundView'].includes(item.fileName))
  .sort((a, b) => a.fileName.localeCompare(b.fileName))
  .map((item) => ({
    label: item.fileName,
    value: item.fileName
  }))
