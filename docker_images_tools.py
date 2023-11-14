import os, subprocess
from sys import exit

# Error handler:
def is_docker_exists():
    try:
        subprocess.run(["docker", "--version"], check=True, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        return True
    except subprocess.CalledProcessError:
        return False
    
docker_ready = is_docker_exists()

if docker_ready:
    print("Docker is installed && available")
else:
    print("Docker is not available. Please install Docker to continue")
    exit(0)

# Customize here:

f = open("file", "r") # Filename with images list (need to be in current dir). Replace "file" with your filename

tar_name = input("Enter result archive name (without .tar): ")
source_repo = "harbor.altezza.org/frisbee/"
target_repo = "registry.msk.cht/tdm/"

# End of customize field

source_registry_content = []
target_registry_content = []

class Images:

    def __init__(self, source_content, target_content):
        self.source_content = source_content
        self.target_content = target_content

    def build_source_content(self):
        for i in f:
            self.source_content.append(i.strip('\n'))
        print(self.source_content)

    def pull_images(self):
        for i in self.source_content:
            os.system(f"docker pull {i}")
        return print(f"Images were pulled.")

    def tag_images(self):
        for i in self.source_content:
            self.target_content.append(i.replace(f'{source_repo}', f'{target_repo}'))
        for i in range(len(self.source_content)):
            print(f"{self.source_content[i]} {self.target_content[i]}")
            os.system(f"docker tag {self.source_content[i]} {self.target_content[i]}")
        print("Images were tagged.")
        return self.target_content

    def save_docker_images(self):
        images_list = " ".join(self.target_content)
        print("Start saving images...")
        os.system(f"docker save {images_list} > {tar_name}.tar")
        print("Images were saved.")
        return images_list

# Object declaration:
docker_object = Images(source_registry_content, target_registry_content)

# Class methods:
docker_object.build_source_content()
docker_object.pull_images()
docker_object.tag_images()
docker_object.save_docker_images()
