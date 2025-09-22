extends Node3D

const FILE_COLOR = Color(0.8, 0.8, 0.8)  # Light Gray
const DIR_COLOR = Color(0.54, 0.27, 0.07)   # Brown
const ITEM_SIZE = 0.25

@onready var bookcase1 = $Room/Furniture/Bookcase1
@onready var bookcase2 = $Room/Furniture/Bookcase2

# Define the local positions for items on the shelves of the 'stall.glb' model.
# These values are estimates and may need tweaking for a perfect fit.
var shelf_slots = [
	# Bottom shelf
	Vector3(-0.8, 0.4, 0), Vector3(-0.5, 0.4, 0), Vector3(-0.2, 0.4, 0), Vector3(0.1, 0.4, 0), Vector3(0.4, 0.4, 0), Vector3(0.7, 0.4, 0),
	# Middle shelf
	Vector3(-0.8, 1.0, 0), Vector3(-0.5, 1.0, 0), Vector3(-0.2, 1.0, 0), Vector3(0.1, 1.0, 0), Vector3(0.4, 1.0, 0), Vector3(0.7, 1.0, 0),
	# Top shelf
	Vector3(-0.8, 1.6, 0), Vector3(-0.5, 1.6, 0), Vector3(-0.2, 1.6, 0), Vector3(0.1, 1.6, 0), Vector3(0.4, 1.6, 0), Vector3(0.7, 1.6, 0)
]

func _ready():
	var path = OS.get_system_dir(OS.SYSTEM_DIR_HOME)

	var dir_access = DirAccess.open(path)
	if dir_access:
		var items = []
		# First, get all directories
		var dirs = dir_access.get_directories()
		for dir in dirs:
			if not dir.begins_with("."):
				items.append({"name": dir, "is_dir": true})

		# Then, get all files
		var files = dir_access.get_files()
		for file in files:
			if not file.begins_with("."):
				items.append({"name": file, "is_dir": false})

		populate_bookcases(items)
	else:
		print("Could not access directory: %s" % path)

func populate_bookcases(items):
	var item_index = 0
	var bookcases = [bookcase1, bookcase2]

	for bookcase in bookcases:
		if !is_instance_valid(bookcase):
			continue

		for slot_position in shelf_slots:
			if item_index >= items.size():
				return # No more items to place

			var item = items[item_index]

			var mesh_instance = MeshInstance3D.new()
			var box_mesh = BoxMesh.new()
			box_mesh.size = Vector3(ITEM_SIZE, ITEM_SIZE, ITEM_SIZE)
			var material = StandardMaterial3D.new()

			if item.is_dir:
				material.albedo_color = DIR_COLOR
			else:
				material.albedo_color = FILE_COLOR

			box_mesh.material = material
			mesh_instance.mesh = box_mesh

			# Add a label to the cube
			var label = Label3D.new()
			label.text = item.name
			label.font_size = 64
			label.autowrap_mode = TextServer.AUTOWRAP_OFF
			label.set_horizontal_alignment(HORIZONTAL_ALIGNMENT_CENTER)
			label.set_vertical_alignment(VERTICAL_ALIGNMENT_CENTER)
			label.billboard = BaseMaterial3D.BILLBOARD_ENABLED
			label.fixed_size = Vector2(200, 50)
			label.transform.origin = Vector3(0, 0.2, 0)
			label.transform.basis = Basis.from_scale(Vector3(0.002, 0.002, 0.002))
			mesh_instance.add_child(label)

			mesh_instance.transform.origin = slot_position

			bookcase.add_child(mesh_instance)

			item_index += 1

func _process(delta):
	pass
