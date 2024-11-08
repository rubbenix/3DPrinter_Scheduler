# Webshop

This simple express server has only 1 end point. /order

It has get/patch/post commands which allow getting all the orders, a specific order, patching it or posting a new one.

For simplicity orders contain only 1 print. As the main goal is proving we can get a print from the webshop to the printers and then update
the order status when a print is done.

In /tests you will a series of tests that can be performed when the server is first started to check if all the routes are behaving accordingly.

The script randomOrderPlacer can be started to randomly start adding different orders. It does this with a second interval and 10% chance each second.
