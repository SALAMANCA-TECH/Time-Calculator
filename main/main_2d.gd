extends Node2D

const FOLDER_ICON = preload("res://assets/button_rectangle_flat.png")
const FILE_ICON = preload("res://assets/button_square_line.png")

@onready var icon_grid = $MainPanel/IconGrid

# Files and extensions to ignore
const IGNORED_NAMES = ["desktop.ini", "Thumbs.db", "ntuser.dat"]
const IGNORED_EXTENSIONS = ["lnk"]

func _ready():
	var path = OS.get_system_dir(OS.SYSTEM_DIR_DOCUMENTS)

	var dir_access = DirAccess.open(path)
	if dir_access:
		var items = []

		# Get directories
		var dirs = dir_access.get_directories()
		for dir in dirs:
			if not should_ignore(dir):
				items.append({"name": dir, "is_dir": true})

		# Get files
		var files = dir_access.get_files()
		for file in files:
			if not should_ignore(file):
				items.append({"name": file, "is_dir": false})

		populate_grid(items)
	else:
		print("Could not access directory: %s" % path)

func should_ignore(name: String) -> bool:
	if name.begins_with("."):
		return true
	if name.to_lower() in IGNORED_NAMES:
		return true
	var extension = name.get_extension().to_lower()
	if extension in IGNORED_EXTENSIONS:
		return true
	return false

func populate_grid(items):
	for item in items:
		var button = TextureButton.new()
		if item.is_dir:
			button.texture_normal = FOLDER_ICON
		else:
			button.texture_normal = FILE_ICON

		button.pressed.connect(func(): _on_item_pressed(item.name))

		var label = Label.new()
		label.text = item.name
		label.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER

		var vbox = VBoxContainer.new()
		vbox.add_child(button)
		vbox.add_child(label)

		icon_grid.add_child(vbox)

func _on_item_pressed(item_name: String):
	print("Selected: %s" % item_name)

func _process(delta):
	pass
