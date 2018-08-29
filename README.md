# Template for Odysseus Subrepos

This is a template that can be used to create plugins for the Odysseus system. Fork this template repository.

Content
-------
- client 
	- contains everything with a gui and can have rcp dependencies. 
	- Plugins from here are only allowed to have dependencies to projects in common and resource folder.
- common 
	- everything that must be shared between client and server must be placed here. 
	- Plugins from here are only allowed to have dependencies to projects in resource folder.
- monolithic 
	- for cases where both client and server plugins are required, these plugins can be used in the monolithic product 
	- Plugins from here are allowed to have dependencies to projects in client, common, resource, server and wrapper folder.
- resource 
	- if necessary, place dependent libraries here, typically they should be imported by an update site, but sometimes this is not possible 
	- Plugins from here are not allowed to have any dependencies to projects outside this folder.
- server 
	- contains everything that can be used on a server, i.e. does not have any gui or direct user interaction 
	- Plugins from here are only allowed to have dependencies to projects in common and resource folder.
- test
	- todo ... 
- wrapper 
	- special server plugins that provide access to external resources 
	- Plugins from here are only allowed to have dependencies to projects in common, resource and server folder.  
- odysseus_dev:
	- This git submodule contains the current target platform and products to start a client, a server or a monolithic version of Odysseus
	- copy content to own folder, if changes are necessary that should not be reflected globally (i.e. for all Odysseus related plugins)

  
