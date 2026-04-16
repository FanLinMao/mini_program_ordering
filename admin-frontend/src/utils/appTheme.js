function clamp(value, min = 0, max = 255) {
  return Math.min(max, Math.max(min, value))
}

function normalizeHex(color) {
  if (!color) return '#409EFF'
  const value = color.trim()
  if (/^#([0-9a-fA-F]{3}){1,2}$/.test(value)) {
    if (value.length === 4) {
      return `#${value[1]}${value[1]}${value[2]}${value[2]}${value[3]}${value[3]}`
    }
    return value
  }
  return '#409EFF'
}

function hexToRgb(color) {
  const hex = normalizeHex(color).replace('#', '')
  return {
    r: parseInt(hex.slice(0, 2), 16),
    g: parseInt(hex.slice(2, 4), 16),
    b: parseInt(hex.slice(4, 6), 16)
  }
}

function rgbToHex({ r, g, b }) {
  const toHex = (value) => clamp(value).toString(16).padStart(2, '0')
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`
}

function mix(color, target, weight) {
  const sourceRgb = hexToRgb(color)
  const targetRgb = hexToRgb(target)
  return rgbToHex({
    r: Math.round(sourceRgb.r + (targetRgb.r - sourceRgb.r) * weight),
    g: Math.round(sourceRgb.g + (targetRgb.g - sourceRgb.g) * weight),
    b: Math.round(sourceRgb.b + (targetRgb.b - sourceRgb.b) * weight)
  })
}

export function applyGlobalThemeColor(color) {
  const root = document.documentElement
  const primary = normalizeHex(color)
  const light3 = mix(primary, '#ffffff', 0.3)
  const light5 = mix(primary, '#ffffff', 0.5)
  const light7 = mix(primary, '#ffffff', 0.7)
  const light8 = mix(primary, '#ffffff', 0.8)
  const light9 = mix(primary, '#ffffff', 0.9)
  const dark2 = mix(primary, '#000000', 0.2)

  root.style.setProperty('--app-theme-color', primary)
  root.style.setProperty('--el-color-primary', primary)
  root.style.setProperty('--el-color-primary-light-3', light3)
  root.style.setProperty('--el-color-primary-light-5', light5)
  root.style.setProperty('--el-color-primary-light-7', light7)
  root.style.setProperty('--el-color-primary-light-8', light8)
  root.style.setProperty('--el-color-primary-light-9', light9)
  root.style.setProperty('--el-color-primary-dark-2', dark2)

  root.style.setProperty('--el-color-warning', primary)
  root.style.setProperty('--el-color-warning-light-3', light3)
  root.style.setProperty('--el-color-warning-light-5', light5)
  root.style.setProperty('--el-color-warning-light-7', light7)
  root.style.setProperty('--el-color-warning-light-8', light8)
  root.style.setProperty('--el-color-warning-light-9', light9)
  root.style.setProperty('--el-color-warning-dark-2', dark2)
}

export function getDefaultThemeColor() {
  return '#409EFF'
}

export function applyGlobalBackgroundImage(imageUrl) {
  const root = document.documentElement
  const hasImage = Boolean(imageUrl && imageUrl.trim())

  if (hasImage) {
    const safeUrl = imageUrl.trim().replace(/"/g, '\\"')
    root.style.setProperty(
      '--app-background-image',
      `linear-gradient(rgba(16, 24, 38, 0.26), rgba(16, 24, 38, 0.16)), url("${safeUrl}")`
    )
    root.style.setProperty('--app-layout-background', 'rgba(245, 247, 251, 0.82)')
  } else {
    root.style.setProperty(
      '--app-background-image',
      'linear-gradient(180deg, #fff7df 0%, #f7f1e7 48%, #f5efe6 100%)'
    )
    root.style.setProperty('--app-layout-background', '#f5f7fb')
  }
}
