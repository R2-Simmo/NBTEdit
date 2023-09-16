# In-Game NBTEdit / 游戏内NBT编辑器
A Minecraft mod allows you to edit any NBT tags of the game content with a GUI while in-game. Such as TileEntities, Entities. It may help map creators to make custom items or help mod creators to debug.  
本模组可以用于在游戏内编辑物品、实体或方块的 NBT ，可能会对地图制作者制作自定义物品或模组开发者 Debug 有所帮助。

**Forge, NeoForge, Fabric are supported!**  
**Forge、NeoForge、Fabric 均已支持！**

## Usage（食用方法）

### Shortcuts（快捷键）
Press `N` (by default) to edit your target BlockEntity, Entity or ItemStack in main hand (if target is missing).  
使用 `N` 键（默认情况下）打开编辑界面。编辑的目标为十字准星指向的方块实体或者实体，如果没有指向则编辑主手上的物品。  

- `Ctrl` + `C` to Copy a node. （复制）  
- `Ctrl` + `V` to Paste a node. （粘贴）  
- `Ctrl` + `X` to Cut a node. （剪切）  
- `Ctrl` + `D` to Delete a node. （删除）  

### Commands（命令）

- `/nbtedit me`  
Edit player themselves.  
编辑玩家自身。

- `/nbtedit hand`  
Edit ItemStack in player's main hand.  
编辑玩家主手上的物品。

- `/nbtedit <x> <y> <z>`  
Edit BlockEntity at x y z.  
编辑位于 x y z 的方块实体。

- `/nbtedit <entity selector>`  
Edit Entity with entity selector.  
编辑由 entity selector 选择的实体。

### Permissions（权限）
`nbtedit.use`:  
The permission to use NBTEdit. Server operators have this permission by default.  
使用 NBTEdit 的权限。默认服务器OP拥有。  
**Permission nodes above are Forge only**, Fabric OP(permission level >= 2) have this permission by default, change it in configuration files.  
**上述权限节点是 Forge 限定**，Fabric 下默认权限等级大于等于 2 的 OP 拥有，可在配置文件中调整。

### Configurations（配置文件）

#### Forge
Location（位置）: `./config/nbtedit.toml`

| Settings（配置选项）         | Description                             | 说明                      |
|------------------------|-----------------------------------------|-------------------------|
| can_edit_other_players | Allow edit other player in multiplayer. | 是否允许对其他玩家使用 NBTEdit 编辑。 |
| debug                  | Enable debug logs.                      | 是否启用 Debug 日志。          |

#### Fabric
Location（位置）: `./config/nbtedit.json`

| Settings（配置选项）  | Description                             | 说明                      |
|-----------------|-----------------------------------------|-------------------------|
| canEditOthers   | Allow edit other player in multiplayer. | 是否允许对其他玩家使用 NBTEdit 编辑。 |
| permissionLevel | Permission level to use NBTEdit.        | 使用 NBTEdit 所需的权限等级。     |
| debug           | Enable debug logs.                      | 是否启用 Debug 日志。          |


## Origin（原帖地址） 
http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1286750-in-game-nbtedit-edit-mob-spawners-attributes-in

## Screenshots（使用截图）
![使用截图 #1](https://github.com/qyl27/NBTEdit/raw/1.19.4/img/2.png)  
![使用截图 #2](https://github.com/qyl27/NBTEdit/raw/1.19.4/img/3.png)

## Common issues（常见问题）
- I was kicked when I tried to save my edit（在尝试保存时被服务器踢出）:  
    If it shows `Payload may not be larger than 32767 bytes`, please use [Packet Fixer](https://www.curseforge.com/minecraft/mc-mods/packet-fixer) by [TonimatasDEV](https://github.com/TonimatasDEV) to fix it.    
    如果客户端显示 `Payload may not be larger than 32767 bytes`，请使用 [TonimatasDEV](https://github.com/TonimatasDEV) 的 [Packet Fixer](https://www.curseforge.com/minecraft/mc-mods/packet-fixer) 修复。


## Bug report（Bug反馈/催更）
Please go to the issues page.  
请到 Issues 页面提出。
