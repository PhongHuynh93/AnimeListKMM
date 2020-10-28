//
//  ExtensionUtil.swift
//  iosApp
//
//  Created by Coder on 10/25/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation

// https://medium.com/@azamsharp/building-grid-layout-in-swiftui-bcd3bc9627af
extension Array {
    func chunked(into size:Int) -> [[Element]] {
        
        var chunkedArray = [[Element]]()
        
        for index in 0...self.count {
            if index % size == 0 && index != 0 {
                chunkedArray.append(Array(self[(index - size)..<index]))
            } else if(index == self.count) {
                chunkedArray.append(Array(self[index - 1..<index]))
            }
        }
        
        return chunkedArray
    }
}
