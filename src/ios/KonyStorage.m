/********* KonyStorage.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface KonyStorage : CDVPlugin {
  // Member variables go here.
}

- (void)get:(CDVInvokedUrlCommand*)command;
@end

@implementation KonyStorage

- (void)keySet:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;
    NSArray* response = [self obtaingKeySet];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)get:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* key = [command.arguments objectAtIndex:0];

    if (key != nil && [key length] > 0) {
        NSString * response = [self obtaingKonyValuesByKey: key];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:response];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (NSArray *)obtaingKeySet
{
    // Get Resources Path
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *resourcePath = [documentsDirectory stringByAppendingPathComponent:@"/HTML5LOCALSTORAGE/"];

    //List File names
    NSError * error;
    return [[NSFileManager defaultManager] contentsOfDirectoryAtPath:resourcePath error:&error];
}

- (NSString *)obtaingKonyValuesByKey: (NSString*)searchedKey
{
    NSLog(@"Get Resources Path");
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *resourcePath = [documentsDirectory stringByAppendingPathComponent:@"/HTML5LOCALSTORAGE/"];

    NSDictionary *plistDictionary;
    NSString * filePath;

    NSLog(@"List File names");
    NSError * error;
    NSArray * directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:resourcePath error:&error];

    NSLog(@"Starting to search: %@ param. Unarchive files", searchedKey);
    for (id fileName in directoryContents) {
        if([fileName isEqualToString:searchedKey]) {
            NSLog(@"Key mached");
            filePath = [resourcePath stringByAppendingPathComponent:fileName];
            plistDictionary = [NSKeyedUnarchiver unarchiveObjectWithFile:filePath];

            if([plistDictionary[@"Data"] isKindOfClass:[NSDictionary class]]) {
                NSLog(@"Parsing NSDictionary to json");
                NSError *error;
                NSData *jsonData = [NSJSONSerialization dataWithJSONObject:plistDictionary[@"Data"]
                                                        options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                        error:&error];
                if (! jsonData) {
                    NSLog(@"Got an error: %@", error);
                } else {
                    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                    jsonString = [jsonString stringByReplacingOccurrencesOfString:@"\\\/" withString:@"/"];
                    NSLog(@"Returning json data");
                    return jsonString;
                }
            } else {
                NSLog(@"Returning string data");
                return plistDictionary[@"Data"];
            }
        }
    }
    NSLog(@"Key %@ not found", searchedKey);
    return @"";
}

@end
