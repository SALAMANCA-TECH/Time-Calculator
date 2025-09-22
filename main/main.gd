extends Node3D

const FILE_COLOR = Color(1.0, 1.0, 1.0)  # White
const DIR_COLOR = Color(0.5, 0.5, 1.0)   # Blue

const ITEM_SIZE = 1.0
const ITEM_SPACING = 0.5
const COLUMNS = 10

func _ready():
	var path = OS.get_system_dir(OS.SYSTEM_DIR_HOME)

	var dir_access = DirAccess.open(path)
	if dir_access:
		var items = []
		var dirs = dir_access.get_directories()
		for dir in dirs:
			# We'll ignore dotfiles for now to keep the scene clean
			if not dir.begins_with("."):
				items.append({"name": dir, "is_dir": true})

		var files = dir_access.get_files()
		for file in files:
			if not file.begins_with("."):
				items.append({"name": file, "is_dir": false})

		generate_visuals(items)
	else:
		print("Could not access directory: %s" % path)

func generate_visuals(items):
	var x = 0
	var z = 0
	for i in range(items.size()):
		var item = items[i]

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

		var pos_x = (x * (ITEM_SIZE + ITEM_SPACING)) - ((COLUMNS / 2.0) * (ITEM_SIZE + ITEM_SPACING))
		var pos_z = z * (ITEM_SIZE + ITEM_SPACING)
		mesh_instance.transform.origin = Vector3(pos_x, ITEM_SIZE / 2.0, -pos_z)

		add_child(mesh_instance)

		x += 1
		if x >= COLUMNS:
			x = 0
			z += 1

func _process(delta):
	pass
