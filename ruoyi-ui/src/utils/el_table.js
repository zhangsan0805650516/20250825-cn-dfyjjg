/**
 * el-table扩展工具  -- 列宽度自适应
 * @param {*} prop 字段名称(string)
 * @param {*} records table数据列表集(array)
 * @returns 列宽(int)
 */
export function getColumnWidth(prop, records) {
  const minWidth = 80; // 最小宽度
  const padding = 10; // 列内边距

  const contentWidths = records.map((item) => {
    const value = item[prop] ? String(item[prop]) : "";
    const textWidth = getTextWidth(value);
    return textWidth + padding;
  });

  const maxWidth = Math.max(...contentWidths);
  return Math.max(minWidth, maxWidth);
}
/**
 * el-table扩展工具  -- 列宽度自适应 - 获取列宽内文本宽度
 * @param {*} text 文本内容
 * @returns 文本宽度(int)
 */
function getTextWidth(text) {
  const span = document.createElement("span");
  span.style.visibility = "hidden";
  span.style.position = "absolute";
  span.style.top = "-9999px";
  span.style.whiteSpace = "nowrap";
  span.innerText = text;
  document.body.appendChild(span);
  const width = span.offsetWidth + 5;
  document.body.removeChild(span);
  return width;
}

// ...其他方法
