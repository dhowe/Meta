from pywebcopy import save_website

kwargs = {'project_name': 'some-fancy-name'}

save_website(
    url='https://rednoise.org/wdm/',
    project_folder='./downloads',
    **kwargs
)
