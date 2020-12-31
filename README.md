# GA_JSON_InsertRelativeFilePath
JSON editor, enabling the export of media files along with file metadata details from Griffeye Analyze.  

Step 1: The user must export all desired media files to a single directory named "Files", with filesnames representing their MD5.  
Step 2: The user must remove any file extensions generated from step 1, (windows commandline from directory containing files: "ren * *.").  
Step 3: The user must export a JSON, selecting the same media files from step 1, exporting only 'categories' and 'source id' fields.  
Step 4: The user runs this program against the generated JSON file from step 3, (command line usage: java -jar GA_JSON_InsertRelativeFilePath.jar "inputfilename.json")
Step 5: The user places the resulting JSON file in the parent directory of the aforementioned "Files" directory.  

Outputs to same directory as input file, named "inputfilename_output.json", respectively.  

Links JSON file to MD5 named files located at JSON_file_path\Files\

When importing, do not use local database/GID for categories, which are already present in JSON.  
