import json

# 从 input.txt 文件中以 UTF-8 编码读取 JSON 字符串
with open('input.txt', 'r', encoding='utf-8') as f:
    json_array_string = f.read()

# 解析JSON数组
json_array = json.loads(json_array_string)

# 用于存放结果的列表
result_list = []

# 遍历JSON数组中的每个对象
for item in json_array:
    # 用于存放当前对象的处理结果
    item_result = []

    # 遍历每个对象中的键值对
    for key, value in item.items():
        # 如果值是字符串，则添加单引号包围后加入当前对象的处理结果列表
        if isinstance(value, str):
            item_result.append("'" + value + "'")
        # 如果值是数字，则直接添加到当前对象的处理结果列表
        elif isinstance(value, (int, float)):
            item_result.append(str(value))

    # 将当前对象的处理结果连接成字符串，然后加入结果列表
    result_list.append(', '.join(item_result))

# 将结果列表中的每项结果分行写入文件
with open('output.txt', 'w', encoding='utf-8') as f:
    f.write('\n'.join(result_list))

print("okk")