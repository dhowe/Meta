from pywebcopy import save_website

kwargs = {'project_name': 'wdm'}

save_website(
    url='https://rednoise.org/teaching/wdm/',
    project_folder='./downloads',
    **kwargs
)
