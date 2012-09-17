/*

	jQuery pub/sub plugin by Peter Higgins (dante@dojotoolkit.org), modified by Mike Wright (dunestech.com)

	Loosely based on Dojo publish/subscribe API, limited in scope. Rewritten blindly.

	Original is (c) Dojo Foundation 2004-2009. Released under either AFL or new BSD, see:
	http://dojofoundation.org/license for more information.

*/

(function(d){
	// the topic/subscription hash
	var cache = {};

    d.publish = function(/* String */topic, /* Array? */args, /*Event*/preventDefaultEvent){
        if (cache[topic]) {
            d.each(cache[topic], function(){
                if (this.callback) this.callback.apply(d, args || []);
            });
        }
        if (preventDefaultEvent) preventDefaultEvent.preventDefault();
	};

	d.subscribe = function(/* String */topic, /* String */ viewName, /* Function */callback) {
        // Store subscriptions in an array
        if (!viewName || viewName=='') {
            alert('Error: missing view for subscription: ' + topic);
            return false;
        }
// INITIALIZATION
        var _subscription = {view:viewName, callback:callback};
        if (!cache[topic]) { cache[topic]=[ _subscription ]; return false }                             // Initialize the topic and exit if storing the subscription for the first time

        var _found = false;
        $.each(cache[topic], function() {
            if (this.view == viewName) { _found = true; this.callback = callback; return false }        // If the subscription exists, overwrite the existing one
        });
        if (!_found) cache[topic].push( _subscription );
	};

    d.unsubscribeView = function(/* String */ viewName) {
        // Uses the new cache by viewName to find subscriptions.
        $.each(cache, function(idx) {
            $.each(cache[idx], function(subIdx) {
                if (this.view == viewName) cache[idx].splice(subIdx, 1);
            });
        });
    };
    d.unsubscribe = function(/* Array */handle){
		// summary:
		//		Disconnect a subscribed function for a topic.
		// handle: Array
		//		The return value from a $.subscribe call.
		// example:
		//	|	var handle = $.subscribe("/something", function(){});
		//	|	$.unsubscribe(handle);

		var t = handle[0];
		cache[t] && d.each(cache[t], function(idx){
			if(this == handle[1]){
				cache[t].splice(idx, 1);
			}
		});
	};

})(jQuery);