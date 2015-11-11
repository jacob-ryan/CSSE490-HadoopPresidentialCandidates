$(document).ready(function()
{
	$("#update").on("click", function()
	{
		var candidate = $("#candidate").val();
		var type = $("#type").val();

		cleanupTables();

		if (type === "n-gram")
		{
			displayNGram(candidate);
		}
		if (type === "retweet")
		{
			displayRetweet();
		}
		if (type === "storm")
		{
			displayStorm(candidate);
		}
	});

	var cleanupTables = function()
	{
		var tables = $.fn.dataTable.fnTables();
		for (var i = 0; i < tables.length; i += 1)
		{
			$(tables[i]).dataTable().fnDestroy();
		}
	};

	var displayNGram = function(candidate)
	{
		$("#results").html("Loading data for " + candidate + "...");
		$.get("/N-gram/" + candidate + ".txt").done(function(data)
		{
			$("#results").html("<div class='text-center'></div><br><table></table>");

			var lines = data.split("\n");
			var data = {};
			for (var i = 0; i < 10; i += 1)
			{
				data[i + 1] = [];
				$("#results div").append("<button class='btn btn-sm btn-default' data-n='" + (i + 1) + "'>" + (i + 1) + "-grams</button> ");
			}

			for (var i = 0; i < lines.length; i += 1)
			{
				var lineParts = lines[i].split("\t");
				if (lineParts.length === 2)
				{
					var key = lineParts[0];
					var value = lineParts[1];
					var n = key.split(" ").length;

					data[n].push([key, value]);
				}
			}

			$("#results div").on("click", "button", function()
			{
				var n = $(this).attr("data-n");

				$("#results div button").removeClass("active");
				$(this).addClass("active");

				cleanupTables();

				$("#results table").empty();
				$("#results table").dataTable({
					"aaSorting": [[1, "desc"]],
					"aaData": data[n],
					"aoColumns": [
						{
							sTitle: "N-gram"
						},
						{
							sTitle: "Count",
							sWidth: "100px",
							sClass: "text-center"
						}
					]
				});
			});

			/* Add Bootstrap classes to select and input elements added by datatables above the table */
			$('.dataTables_filter input').addClass('form-control').attr('placeholder', 'Search');
			$('.dataTables_length select').addClass('form-control');

			setTimeout(function()
			{
				$("#results button[data-n=2]").click();
			}, 10);
		}).fail(function()
		{
			alert("Error loading N-gram data!");
		});
	};

	var displayRetweet = function()
	{
		$("#results").html("Loading all retweet data...");
		$.get("/Retweet/retweets.txt").done(function(data)
		{
			$("#results").html("<table></table>");

			var lines = data.split("\n");
			var data = [];

			for (var i = 0; i < lines.length; i += 1)
			{
				var lineParts = lines[i].split("\t");
				if (lineParts.length === 2)
				{
					var key = lineParts[0];
					var value = lineParts[1];
					value = parseFloat(value).toFixed(2);

					data.push([key, value]);
				}
			}

			$("#results table").dataTable({
				"aaSorting": [[1, "desc"]],
				"aaData": data,
				"aoColumns": [
					{
						sTitle: "Twitter Handle",
						sClass: ""
					},
					{
						sTitle: "Average Reweets",
						sWidth: "100px",
						sClass: "text-center"
					}
				]
			});

			/* Add Bootstrap classes to select and input elements added by datatables above the table */
			$('.dataTables_filter input').addClass('form-control').attr('placeholder', 'Search');
			$('.dataTables_length select').addClass('form-control');
		}).fail(function()
		{
			alert("Error loading retweet data!");
		});
	};

	var displayStorm = function(candidate)
	{
		$("#results").html("Loading Storm data for " + candidate + "...");
		$.get("/Storm/" + candidate + ".txt").done(function(data)
		{
			$("#results").html("<table></table>");

			var lines = data.split("\n");
			var data = [];

			for (var i = 0; i < lines.length; i += 1)
			{
				var lineParts = lines[i].split("\t");
				if (lineParts.length === 2)
				{
					var key = lineParts[0];
					var value = lineParts[1];

					data.push([key, key.length, value, key.length * value]);
				}
			}

			$("#results table").dataTable({
				"aaSorting": [[3, "desc"]],
				"aaData": data,
				"aoColumns": [
					{
						sTitle: "Word",
						sClass: ""
					},
					{
						sTitle: "Length",
						sWidth: "100px",
						sClass: "text-center"
					},
					{
						sTitle: "Count",
						sWidth: "100px",
						sClass: "text-center"
					},
					{
						sTitle: "Coolness",
						sWidth: "100px",
						sClass: "text-center"
					}
				]
			});

			/* Add Bootstrap classes to select and input elements added by datatables above the table */
			$('.dataTables_filter input').addClass('form-control').attr('placeholder', 'Search');
			$('.dataTables_length select').addClass('form-control');
		}).fail(function()
		{
			alert("Error loading Storm data!");
		});
	};
});