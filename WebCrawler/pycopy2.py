from pywebcopy import Crawler, config


kwargs = {
    'zip_project_folder':False,
    'allowed_file_ext':['.html', '.php', '.asp', '.aspx', '.htm', '.xhtml', '.css', '.json', '.js', '.xml', '.svg', '.gif', '.ico', '.jpeg', '.pdf', '.jpg', '.png', '.ttf', '.eot', '.otf', '.woff', '.woff2', '.pwcf']
}

config.setup_config(project_url='https://rednoise.org/teaching/wdm/', project_folder='./downloads2', project_name='wdm')

crawler = Crawler()
crawler.crawl()

#allowed_file_ext=['.html', '.php', '.asp', '.aspx', '.htm', '.xhtml', '.css', '.json', '.js', '.xml', '.svg', '.gif', '.ico', '.jpeg', '.pdf', '.jpg', '.png', '.ttf', '.eot', '.otf', '.woff', '.woff2', '.pwcf'],
#'over_write':True,  <- does not worked properly
