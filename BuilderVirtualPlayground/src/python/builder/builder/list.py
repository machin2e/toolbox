def list():
	builderfile_path = './Builderfile'

	# Read the Builderfile
	# TODO: Initialize the daemon here?
	file = open(builderfile_path, 'r')
	filelines = file.readlines()
	
	device_uuid = 'N/A'
	device_name = 'N/A'
	device_ip = 'N/A'

	for i in range(len(filelines)):

		# Read UUID
		if filelines[i].startswith('UUID:'):
			device_uuid = filelines[i].split(': ')[1]
			device_uuid = device_uuid.replace('\n', '')

		# Read human-readable name 
		if filelines[i].startswith('Name:'):
			device_name = filelines[i].split(': ')[1]
			device_name = device_name.replace('\n', '')

	# Print the device listing
	print "%s\t%s\t%s" % (device_uuid, device_name, device_ip)

	file.close()

if __name__ == "__main__":
	list()
